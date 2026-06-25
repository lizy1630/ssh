"""Slack output surface — post processed results back to Slack.

Used by the voice API (iPhone path) to report transcripts + outcomes. Only needs
the bot token + ``chat:write`` (no Socket Mode / app token). The Slack listener
posts via its own Bolt client; this is the standalone poster for non-listener code.
"""

from __future__ import annotations

import logging

from .config import config

logger = logging.getLogger(__name__)

_client = None


def _get_client():
    global _client
    if _client is None:
        from slack_sdk import WebClient

        _client = WebClient(token=config.slack_bot_token)
    return _client


def post_to_slack(text: str, *, channel: str | None = None, thread_ts: str | None = None):
    """Post a message to the Slack output channel. Returns the API response or None.

    Failures are logged and swallowed — the result surface is best-effort and must
    never crash the caller (e.g. the voice endpoint).
    """
    target = channel or config.slack_output_channel
    if not target:
        logger.error("No SLACK_OUTPUT_CHANNEL / INBOX_CHANNEL_ID configured; cannot post.")
        return None
    try:
        return _get_client().chat_postMessage(
            channel=target, text=text, thread_ts=thread_ts
        )
    except Exception:  # noqa: BLE001 - best-effort output surface
        logger.exception("Failed to post message to Slack")
        return None
