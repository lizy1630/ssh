"""Tests for transcription provider dispatch — no models are loaded."""

import pytest

from slack_assistant import transcription
from slack_assistant.transcription import (
    TranscriptionError,
    available_providers,
    transcribe,
)


def test_available_providers():
    assert set(available_providers()) == {"sensevoice", "paraformer", "faster-whisper"}


def test_unknown_provider_raises():
    with pytest.raises(TranscriptionError):
        transcribe("/tmp/whatever.wav", provider="does-not-exist")


def test_dispatch_calls_selected_provider(monkeypatch):
    calls = {}

    def fake(path):
        calls["path"] = path
        return "  hello world  "

    monkeypatch.setitem(transcription._PROVIDERS, "sensevoice", fake)
    # Provider impls are expected to return stripped text already; dispatch passes through.
    assert transcribe("/tmp/a.wav", provider="sensevoice") == "  hello world  "
    assert calls["path"] == "/tmp/a.wav"


def test_provider_exception_wrapped(monkeypatch):
    def boom(path):
        raise ValueError("model exploded")

    monkeypatch.setitem(transcription._PROVIDERS, "paraformer", boom)
    with pytest.raises(TranscriptionError) as ei:
        transcribe("/tmp/a.wav", provider="paraformer")
    assert "paraformer transcription failed" in str(ei.value)
