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


def test_intake_channel_accepts_integration_post(monkeypatch):
    # A dedicated intake channel accepts bot/integration posts (Power Automate),
    # which have a bot_id and no user.
    monkeypatch.setenv("INBOX_CHANNEL_ID", "C_INBOX")
    monkeypatch.setenv("ALLOWED_USER_ID", "U_OWNER")
    monkeypatch.setenv("EMAIL_INTAKE_CHANNEL", "C_INTAKE")
    import importlib

    import slack_assistant.config as config_mod

    importlib.reload(config_mod)
    import slack_assistant.listener as listener_mod

    importlib.reload(listener_mod)

    pa_post = {
        "type": "message",
        "channel": "C_INTAKE",
        "bot_id": "B_POWERAUTOMATE",
        "text": "Client email …",
    }
    assert listener_mod.should_handle(
        pa_post, bot_user_id="U_BOT", self_bot_id="B_SELF"
    )
    # but our own bot's post in the intake channel is still ignored (no loop)
    own = {**pa_post, "bot_id": "B_SELF"}
    assert not listener_mod.should_handle(
        own, bot_user_id="U_BOT", self_bot_id="B_SELF"
    )
    # and a third-party bot in the inbox DM is still ignored
    inbox_bot = {"type": "message", "channel": "C_INBOX", "bot_id": "B_X"}
    assert not listener_mod.should_handle(
        inbox_bot, bot_user_id="U_BOT", self_bot_id="B_SELF"
    )


def test_extract_audio_files_by_mimetype(listener):
    event = _msg(files=[{"mimetype": "audio/m4a", "name": "v.m4a"}, {"mimetype": "image/png"}])
    audio = listener._extract_audio_files(event)
    assert len(audio) == 1 and audio[0]["name"] == "v.m4a"
