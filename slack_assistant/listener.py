"""Slack Socket Mode listener — the real-time entry point.

Replaces the old ~10-minute poll: the instant you post in the inbox channel,
this fires, transcribes any voice memo locally, routes the request through the
Claude agent, and replies in-thread with what it did.
"""

from __future__ import annotations

import asyncio
import logging
import os
import tempfile
import threading

import requests

from .agent import append_memory, run as agent_run
from .config import config
from .decisions import log_decision
from .transcription import transcribe

logger = logging.getLogger(__name__)

_AUDIO_SUBTYPES = ("audio", "mp3", "m4a", "wav", "ogg", "mpeg", "x-m4a", "webm")
_IMAGE_SUBTYPES = ("png", "jpg", "jpeg", "gif", "webp", "heic", "bmp", "tiff")


def should_handle(event: dict, *, bot_user_id: str | None) -> bool:
    """Decide whether an incoming Slack message event is one we should act on.

    Filters out bot/self echoes, message edits/deletes, the wrong channel, and
    (when ``ALLOWED_USER_ID`` is set) anyone other than the owner.
    """
    if event.get("type") != "message":
        return False
    # Ignore edits, deletions, joins, and other message subtypes that aren't user text.
    subtype = event.get("subtype")
    if subtype and subtype != "file_share":
        return False
    # Never react to our own bot's messages (prevents loops).
    if event.get("bot_id"):
        return False
    if bot_user_id and event.get("user") == bot_user_id:
        return False
    # Scope to the dedicated inbox channel.
    if config.inbox_channel_id and event.get("channel") != config.inbox_channel_id:
        return False
    # Restrict to the owner if configured.
    if config.allowed_user_id and event.get("user") != config.allowed_user_id:
        return False
    return True


def _extract_audio_files(event: dict) -> list[dict]:
    files = event.get("files") or []
    audio = []
    for f in files:
        mimetype = (f.get("mimetype") or "").lower()
        filetype = (f.get("filetype") or "").lower()
        if mimetype.startswith("audio/") or filetype in _AUDIO_SUBTYPES:
            audio.append(f)
    return audio


def _extract_image_files(event: dict) -> list[dict]:
    files = event.get("files") or []
    images = []
    for f in files:
        mimetype = (f.get("mimetype") or "").lower()
        filetype = (f.get("filetype") or "").lower()
        if mimetype.startswith("image/") or filetype in _IMAGE_SUBTYPES:
            images.append(f)
    return images


def _download_slack_file(file_obj: dict) -> str:
    """Download a Slack-hosted file to a temp path using bot-token auth."""
    url = file_obj.get("url_private_download") or file_obj["url_private"]
    headers = {"Authorization": f"Bearer {config.slack_bot_token}"}
    resp = requests.get(url, headers=headers, timeout=60)
    resp.raise_for_status()
    suffix = os.path.splitext(file_obj.get("name", "audio"))[1] or ".audio"
    fd, path = tempfile.mkstemp(suffix=suffix)
    with os.fdopen(fd, "wb") as fh:
        fh.write(resp.content)
    return path


def _resolve_transcript(event: dict) -> tuple[str, bool]:
    """Return (text, was_voice). Transcribes audio attachments if present."""
    audio_files = _extract_audio_files(event)
    if not audio_files:
        return event.get("text", "").strip(), False
    parts = []
    for f in audio_files:
        path = _download_slack_file(f)
        try:
            parts.append(transcribe(path))
        finally:
            try:
                os.remove(path)
            except OSError:
                pass
    # Append any caption text the user added alongside the voice memo.
    caption = event.get("text", "").strip()
    if caption:
        parts.append(caption)
    return "\n".join(p for p in parts if p).strip(), True


def _process(event: dict, client) -> None:
    """Worker body: transcribe, run the agent, post feedback. Runs off-thread."""
    channel = event["channel"]
    ts = event["ts"]
    try:
        client.reactions_add(channel=channel, timestamp=ts, name="eyes")
    except Exception:  # noqa: BLE001 - feedback is best-effort
        logger.debug("Could not add :eyes: reaction", exc_info=True)

    image_paths: list[str] = []
    try:
        transcript, was_voice = _resolve_transcript(event)
        # Download any image attachments; they must outlive the agent run.
        image_paths = [_download_slack_file(f) for f in _extract_image_files(event)]
        if not transcript and not image_paths:
            _react(client, channel, ts, remove="eyes", add="question")
            return
        if was_voice:
            client.chat_postMessage(
                channel=channel, thread_ts=ts, text=f"📝 Heard: {transcript}"
            )
        # When only an image is sent (no text), give the agent a default instruction.
        agent_text = transcript or "(no text — process the attached image)"

        summary = asyncio.run(
            agent_run(agent_text, {"source": "slack"}, attachments=image_paths or None)
        )

        client.chat_postMessage(
            channel=channel, thread_ts=ts, text=summary or "Done."
        )
        _react(client, channel, ts, remove="eyes", add="white_check_mark")
        log_decision(transcript=transcript or "[image]", summary=summary, ok=True)
    except Exception as exc:  # noqa: BLE001 - never crash the listener thread
        logger.exception("Failed to process message")
        try:
            client.chat_postMessage(
                channel=channel, thread_ts=ts, text=f"⚠️ Sorry, that failed: {exc}"
            )
        except Exception:  # noqa: BLE001
            pass
        _react(client, channel, ts, remove="eyes", add="x")
        log_decision(transcript=event.get("text", ""), summary=str(exc), ok=False)
    finally:
        for path in image_paths:
            try:
                os.remove(path)
            except OSError:
                pass


def _react(client, channel, ts, *, remove: str, add: str) -> None:
    for name, fn in ((remove, client.reactions_remove), (add, client.reactions_add)):
        try:
            fn(channel=channel, timestamp=ts, name=name)
        except Exception:  # noqa: BLE001
            logger.debug("reaction %s failed", name, exc_info=True)


def build_app():
    """Construct the Bolt app and register handlers."""
    from slack_bolt import App

    config.require_slack()
    app = App(token=config.slack_bot_token)
    bot_user_id = app.client.auth_test().get("user_id")
    logger.info("Authenticated as bot user %s", bot_user_id)

    @app.event("message")
    def handle_message(event, client):  # noqa: ANN001
        if not should_handle(event, bot_user_id=bot_user_id):
            return
        # Hand off to a worker thread so we ack the socket immediately.
        threading.Thread(
            target=_process, args=(event, client), daemon=True
        ).start()

    return app


def main() -> None:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s %(levelname)s %(name)s: %(message)s",
    )
    from slack_bolt.adapter.socket_mode import SocketModeHandler

    from .scheduler import start_background_jobs

    app = build_app()
    start_background_jobs()  # daily digest + hourly health check
    logger.info("Starting Slack Socket Mode listener...")
    SocketModeHandler(app, config.slack_app_token).start()


if __name__ == "__main__":
    main()
