"""Hourly health probe. Emails an alert when the service looks unhealthy.

The probe is intentionally cheap and Claude-free for the happy path: it checks
Slack auth, that an MCP config is present, and that the transcription model can
be referenced. Only when something fails does it ask the agent to send an alert
email (so a failure costs one Claude call, a healthy check costs none).

NOTE: an in-process probe cannot detect a fully crashed process. README
documents an external watchdog (systemd ``Restart=`` / cron heartbeat) as the
backstop for that case.
"""

from __future__ import annotations

import asyncio
import logging

from .agent import load_mcp_servers, run_digest
from .config import config
from .transcription import available_providers

logger = logging.getLogger(__name__)


def check_health() -> list[str]:
    """Return a list of failure descriptions. Empty list means healthy."""
    failures: list[str] = []

    # 1. Slack reachable / token valid.
    try:
        from slack_sdk import WebClient

        auth = WebClient(token=config.slack_bot_token).auth_test()
        if not auth.get("ok"):
            failures.append("Slack auth_test returned not-ok.")
    except Exception as exc:  # noqa: BLE001
        failures.append(f"Slack auth_test failed: {exc}")

    # 2. MCP servers configured.
    if not load_mcp_servers():
        failures.append("No MCP servers configured (MCP_CONFIG_PATH missing/empty).")

    # 3. Transcription provider is valid.
    if config.transcribe_provider not in available_providers():
        failures.append(
            f"TRANSCRIBE_PROVIDER {config.transcribe_provider!r} is not valid."
        )

    # 4. Some Claude auth is reachable. Env-based auth (OAuth token / API key) is
    #    verifiable; an interactive `claude` login lives on disk and isn't checked
    #    here, so only warn when no on-disk login exists either.
    if not config.has_claude_auth() and not _has_claude_cli_login():
        failures.append(
            "No Claude auth found: set CLAUDE_CODE_OAUTH_TOKEN (from "
            "`claude setup-token`) or ANTHROPIC_API_KEY, or run `claude` to log in."
        )

    return failures


def _has_claude_cli_login() -> bool:
    """Best-effort check for an interactive Claude Code login on disk."""
    from pathlib import Path

    candidates = [
        Path.home() / ".claude" / ".credentials.json",
        Path.home() / ".config" / "claude" / ".credentials.json",
    ]
    return any(p.exists() for p in candidates)


def _send_alert(failures: list[str]) -> None:
    body = (
        "The Slack assistant health check failed. Problems detected:\n\n"
        + "\n".join(f"- {f}" for f in failures)
        + f"\n\nEmail {config.alert_email_to} with the exact subject "
        f'"{config.alert_email_subject}" describing these problems, '
        "using your Gmail MCP tools. Reply with a one-line confirmation."
    )
    asyncio.run(run_digest(body))


def run_health_check() -> bool:
    """Run the probe; email an alert on failure. Returns True if healthy."""
    failures = check_health()
    if failures:
        logger.warning("Health check FAILED: %s", failures)
        try:
            _send_alert(failures)
        except Exception:  # noqa: BLE001
            logger.exception("Failed to send health alert email")
        return False
    logger.info("Health check OK")
    return True


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)
    run_health_check()
