"""Central configuration for the Slack assistant.

All runtime configuration comes from environment variables (see ``.env.example``).
Loading is done once at import time so every module shares the same values.
"""

from __future__ import annotations

import os
from dataclasses import dataclass, field

try:
    from dotenv import load_dotenv

    load_dotenv()
except ImportError:  # dotenv is optional at runtime if env is already set
    pass


def _get(name: str, default: str | None = None, *, required: bool = False) -> str | None:
    value = os.environ.get(name, default)
    if required and not value:
        raise RuntimeError(
            f"Missing required environment variable: {name}. See .env.example."
        )
    return value


@dataclass(frozen=True)
class Config:
    # --- Slack ---
    slack_bot_token: str | None = field(default_factory=lambda: _get("SLACK_BOT_TOKEN"))
    slack_app_token: str | None = field(default_factory=lambda: _get("SLACK_APP_TOKEN"))
    # The dedicated inbox channel the bot listens to (a private channel you created,
    # or the bot DM). A Slack bot CANNOT read your personal self-DM, so use a channel.
    inbox_channel_id: str | None = field(default_factory=lambda: _get("INBOX_CHANNEL_ID"))
    # Only act on messages authored by this Slack user id (your id). Empty = any human.
    allowed_user_id: str | None = field(default_factory=lambda: _get("ALLOWED_USER_ID"))

    # --- Claude ---
    anthropic_api_key: str | None = field(
        default_factory=lambda: _get("ANTHROPIC_API_KEY")
    )

    # --- Transcription ---
    transcribe_provider: str = field(
        default_factory=lambda: _get("TRANSCRIBE_PROVIDER", "sensevoice")
    )

    # --- Daily digest ---
    digest_hour: int = field(default_factory=lambda: int(_get("DIGEST_HOUR", "21")))
    digest_email_to: str = field(
        default_factory=lambda: _get("DIGEST_EMAIL_TO", "lizy1630@gmail.com")
    )
    digest_email_subject: str = field(
        default_factory=lambda: _get("DIGEST_EMAIL_SUBJECT", "Day Summary")
    )

    # --- Health check ---
    healthcheck_hour_interval: int = field(
        default_factory=lambda: int(_get("HEALTHCHECK_HOUR_INTERVAL", "1"))
    )
    alert_email_to: str = field(
        default_factory=lambda: _get("ALERT_EMAIL_TO", "lizy1630@gmail.com")
    )
    alert_email_subject: str = field(
        default_factory=lambda: _get(
            "ALERT_EMAIL_SUBJECT", "[ALERT] Slack assistant unhealthy"
        )
    )

    def require_slack(self) -> None:
        _get("SLACK_BOT_TOKEN", required=True)
        _get("SLACK_APP_TOKEN", required=True)
        if not self.inbox_channel_id:
            raise RuntimeError("INBOX_CHANNEL_ID is required to scope the listener.")


config = Config()
