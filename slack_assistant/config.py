"""Central configuration for the Slack assistant.

All runtime configuration comes from environment variables (see ``.env.example``).
Loading is done once at import time so every module shares the same values.
"""

from __future__ import annotations

import os
from dataclasses import dataclass, field
from pathlib import Path

try:
    from dotenv import load_dotenv

    # Load slack_assistant/.env explicitly so it works regardless of the current
    # working directory. Fall back to the default search (CWD upward) otherwise.
    _ENV_PATH = Path(__file__).resolve().parent / ".env"
    if _ENV_PATH.exists():
        load_dotenv(_ENV_PATH)
    else:
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
    # Optional second channel the listener also watches — e.g. a dedicated
    # #email-intake channel where Power Automate posts client emails "as you".
    email_intake_channel: str | None = field(
        default_factory=lambda: _get("EMAIL_INTAKE_CHANNEL")
    )

    # --- Claude auth (pick ONE) ---
    # Preferred for Max/Pro subscribers: a long-lived OAuth token from
    # `claude setup-token` (no per-call API billing, static like a key).
    claude_code_oauth_token: str | None = field(
        default_factory=lambda: _get("CLAUDE_CODE_OAUTH_TOKEN")
    )
    # Alternative: pay-as-you-go API key. Leave unset if using the OAuth token
    # or an interactive `claude` login on the host.
    anthropic_api_key: str | None = field(
        default_factory=lambda: _get("ANTHROPIC_API_KEY")
    )

    def has_claude_auth(self) -> bool:
        """True if some Claude auth is configured via env. An interactive
        `claude` login (credentials on disk) is NOT visible here, so a False
        result is a warning, not a hard error."""
        return bool(self.claude_code_oauth_token or self.anthropic_api_key)

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

    # --- Voice API (iPhone -> Mac Mini receiver) ---
    # Bind to the Tailscale interface IP in production so the port is not reachable
    # off-tailnet. Defaults to localhost for safe local testing.
    voice_api_host: str = field(
        default_factory=lambda: _get("VOICE_API_HOST", "auto")
    )
    voice_api_port: int = field(
        default_factory=lambda: int(_get("VOICE_API_PORT", "8765"))
    )
    # Slack channel that receives processed-voice results (the output/log surface).
    # Defaults to the same inbox channel the listener watches.
    slack_output_channel: str | None = field(
        default_factory=lambda: _get("SLACK_OUTPUT_CHANNEL") or _get("INBOX_CHANNEL_ID")
    )
    max_upload_mb: int = field(
        default_factory=lambda: int(_get("MAX_UPLOAD_MB", "25"))
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
