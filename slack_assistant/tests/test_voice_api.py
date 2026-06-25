"""Tests for the /voice FastAPI endpoint — transcribe/agent/Slack are mocked."""

import os

import pytest

fastapi_testclient = pytest.importorskip("fastapi.testclient")
from fastapi.testclient import TestClient  # noqa: E402


@pytest.fixture
def client(monkeypatch):
    import slack_assistant.voice_api as voice_api

    captured = {"posts": [], "removed": []}

    # Track temp-file removal to assert cleanup.
    real_remove = os.remove

    def tracking_remove(path):
        captured["removed"].append(path)
        return real_remove(path)

    monkeypatch.setattr(voice_api.os, "remove", tracking_remove)
    monkeypatch.setattr(voice_api, "transcribe", lambda path: "hello world")

    async def fake_run(text, ctx, attachments=None):
        captured["last_text"] = text
        captured["last_ctx"] = ctx
        return "Created calendar event."

    monkeypatch.setattr(voice_api.agent, "run", fake_run)
    monkeypatch.setattr(
        voice_api.notify, "post_to_slack", lambda text, **k: captured["posts"].append(text)
    )
    monkeypatch.setattr(voice_api, "log_decision", lambda **k: None)

    return TestClient(voice_api.app), captured


def test_health(client):
    c, _ = client
    assert c.get("/health").json() == {"ok": True}


def test_voice_happy_path_raw_body(client):
    c, captured = client
    resp = c.post(
        "/voice", content=b"FAKEAUDIOBYTES", headers={"Content-Type": "audio/m4a"}
    )
    assert resp.status_code == 200
    body = resp.json()
    assert body == {
        "ok": True,
        "transcript": "hello world",
        "summary": "Created calendar event.",
    }
    # Slack was used as the output surface.
    assert captured["posts"] and "hello world" in captured["posts"][0]
    # Temp file was cleaned up.
    assert captured["removed"]
    assert captured["last_ctx"]["source"] == "iphone-voice"


def test_voice_rejects_empty_body(client):
    c, _ = client
    resp = c.post("/voice", content=b"", headers={"Content-Type": "audio/m4a"})
    assert resp.status_code == 400


def test_voice_rejects_unknown_content_type(client):
    c, _ = client
    resp = c.post("/voice", content=b"data", headers={"Content-Type": "text/plain"})
    assert resp.status_code == 415


def test_voice_rejects_oversize(client, monkeypatch):
    import types

    import slack_assistant.voice_api as voice_api

    # config is a frozen dataclass, so swap the module reference for a stub.
    monkeypatch.setattr(voice_api, "config", types.SimpleNamespace(max_upload_mb=0))
    c, _ = client
    resp = c.post("/voice", content=b"x" * 1024, headers={"Content-Type": "audio/m4a"})
    assert resp.status_code == 413
