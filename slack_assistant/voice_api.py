"""FastAPI receiver for the iPhone voice-intake path.

Flow: iPhone Back Tap -> Shortcut records audio -> POST the m4a bytes here (over
Tailscale) -> transcribe locally -> Claude agent routes/executes -> result is
posted to Slack (the output/log surface) and logged to decisions.jsonl.

Run with:
    uvicorn slack_assistant.voice_api:app --host $VOICE_API_HOST --port $VOICE_API_PORT

Auth is Tailscale-only by design: bind VOICE_API_HOST to the Mac's Tailscale IP so
the port is unreachable off-tailnet. See README for the security notes.
"""

from __future__ import annotations

import logging
import os
import tempfile

from fastapi import FastAPI, Request, UploadFile
from fastapi.responses import JSONResponse

from . import agent, notify
from .config import config
from .decisions import log_decision
from .transcription import transcribe

logger = logging.getLogger(__name__)

app = FastAPI(title="Slack assistant voice receiver")

# Accepted audio content-types / extensions (Shortcuts records m4a by default).
_ALLOWED_EXT = {".m4a", ".mp4", ".wav", ".aac", ".ogg", ".caf", ".mp3"}
_CT_TO_EXT = {
    "audio/m4a": ".m4a",
    "audio/x-m4a": ".m4a",
    "audio/mp4": ".m4a",
    "audio/aac": ".aac",
    "audio/wav": ".wav",
    "audio/x-wav": ".wav",
    "audio/wave": ".wav",
    "audio/ogg": ".ogg",
    "audio/mpeg": ".mp3",
    "audio/mp3": ".mp3",
    "audio/x-caf": ".caf",
}


def _suffix_for(content_type: str | None, filename: str | None) -> str | None:
    if filename:
        ext = os.path.splitext(filename)[1].lower()
        if ext in _ALLOWED_EXT:
            return ext
    if content_type:
        ext = _CT_TO_EXT.get(content_type.split(";")[0].strip().lower())
        if ext:
            return ext
    return None


@app.get("/health")
async def health() -> dict:
    return {"ok": True}


@app.post("/voice")
async def voice(request: Request, file: UploadFile | None = None) -> JSONResponse:
    """Accept an audio upload, transcribe, route through Claude, post to Slack."""
    # Support both multipart (file field) and raw-body (Shortcuts "Request Body: File").
    if file is not None:
        data = await file.read()
        suffix = _suffix_for(file.content_type, file.filename)
    else:
        data = await request.body()
        suffix = _suffix_for(request.headers.get("content-type"), None)

    if not data:
        return JSONResponse(status_code=400, content={"ok": False, "error": "empty body"})

    max_bytes = config.max_upload_mb * 1024 * 1024
    if len(data) > max_bytes:
        return JSONResponse(
            status_code=413,
            content={"ok": False, "error": f"file exceeds {config.max_upload_mb} MB"},
        )

    if suffix is None:
        return JSONResponse(
            status_code=415,
            content={"ok": False, "error": "unsupported or missing audio content-type"},
        )

    fd, path = tempfile.mkstemp(suffix=suffix)
    try:
        with os.fdopen(fd, "wb") as fh:
            fh.write(data)

        text = transcribe(path)
        if not text:
            return JSONResponse(
                status_code=422, content={"ok": False, "error": "no speech detected"}
            )

        summary = await agent.run(text, {"source": "iphone-voice"})

        notify.post_to_slack(f"🎙️ {text}\n→ {summary or 'Done.'}")
        log_decision(transcript=text, summary=summary, ok=True)
        return JSONResponse(content={"ok": True, "transcript": text, "summary": summary})
    except Exception as exc:  # noqa: BLE001 - report failure to caller + Slack
        logger.exception("Voice processing failed")
        notify.post_to_slack(f"❌ Voice note failed: {exc}")
        log_decision(transcript="[voice]", summary=str(exc), ok=False)
        return JSONResponse(status_code=500, content={"ok": False, "error": str(exc)})
    finally:
        try:
            os.remove(path)
        except OSError:
            pass
