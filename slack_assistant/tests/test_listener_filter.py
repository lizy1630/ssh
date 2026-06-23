"""Tests for the message filter — pure logic, no Slack/Claude/model needed."""

import importlib

import pytest


@pytest.fixture
def listener(monkeypatch):
    # Configure scope before importing the module so config picks it up.
    monkeypatch.setenv("INBOX_CHANNEL_ID", "C_INBOX")
    monkeypatch.setenv("ALLOWED_USER_ID", "U_OWNER")
    config_mod = importlib.import_module("slack_assistant.config")
    importlib.reload(config_mod)
    listener_mod = importlib.import_module("slack_assistant.listener")
    importlib.reload(listener_mod)
    return listener_mod


def _msg(**over):
    base = {"type": "message", "channel": "C_INBOX", "user": "U_OWNER", "text": "hi"}
    base.update(over)
    return base


def test_accepts_owner_message_in_inbox(listener):
    assert listener.should_handle(_msg(), bot_user_id="U_BOT") is True


def test_accepts_voice_memo_file_share(listener):
    assert listener.should_handle(_msg(subtype="file_share"), bot_user_id="U_BOT")


def test_rejects_wrong_channel(listener):
    assert not listener.should_handle(_msg(channel="C_OTHER"), bot_user_id="U_BOT")


def test_rejects_other_user(listener):
    assert not listener.should_handle(_msg(user="U_SOMEONE"), bot_user_id="U_BOT")


def test_rejects_bot_messages(listener):
    assert not listener.should_handle(_msg(bot_id="B123"), bot_user_id="U_BOT")
    assert not listener.should_handle(_msg(user="U_BOT"), bot_user_id="U_BOT")


def test_rejects_edits_and_deletes(listener):
    assert not listener.should_handle(
        _msg(subtype="message_changed"), bot_user_id="U_BOT"
    )
    assert not listener.should_handle(
        _msg(subtype="message_deleted"), bot_user_id="U_BOT"
    )


def test_extract_audio_files_by_mimetype(listener):
    event = _msg(files=[{"mimetype": "audio/m4a", "name": "v.m4a"}, {"mimetype": "image/png"}])
    audio = listener._extract_audio_files(event)
    assert len(audio) == 1 and audio[0]["name"] == "v.m4a"
