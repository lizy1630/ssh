"""Daily and weekly digests.

Recaps what the assistant handled in the period and the upcoming reminders/
events, then emails a prioritized summary to you. Both run as one Claude Agent
SDK call that reads the calendar (and Tasks, if enabled) via MCP and sends the
email via Gmail.

Run manually:
    python -m slack_assistant.digest          # daily
    python -m slack_assistant.digest weekly   # weekly
"""

from __future__ import annotations

import asyncio
import logging
import sys
from datetime import datetime

from .agent import run_digest
from .config import config
from .decisions import read_decisions_for_day, read_decisions_since

logger = logging.getLogger(__name__)


def _format_records(records: list[dict]) -> list[str]:
    if not records:
        return ["- (no logged assistant activity in this period)"]
    out = []
    for r in records:
        status = "ok" if r.get("ok") else "FAILED"
        ts = str(r.get("ts", ""))[:16].replace("T", " ")
        out.append(f"- [{status}] {ts}  {r.get('summary') or r.get('transcript')}")
    return out


def _build_input(
    *, period_label: str, records: list[dict], lookahead_days: int,
    subject: str, email_to: str,
) -> str:
    lines = [
        f"It is {datetime.now():%A, %Y-%m-%d %H:%M}.",
        f"This is the **{period_label}** digest.",
        "",
        f"Activity the assistant handled this period:",
        *_format_records(records),
        "",
        "Produce the digest:",
        f"1. Read upcoming calendar events/reminders for the next {lookahead_days} "
        "days via your Calendar MCP tools (and Google Tasks if available).",
        "2. Prioritize the logged notes/tasks/actions above into a clear ranked "
        "order, flagging anything time-sensitive or already on the calendar.",
        "3. List upcoming reminders/events in chronological order with their "
        "dates/times so nothing is forgotten.",
        "4. If client emails were handled, summarize them and any drafted replies.",
        f'5. Email the result to {email_to} with the exact subject "{subject}" '
        "via your Gmail MCP tools. Use clear, skimmable sections "
        "(Priorities / On the calendar / Upcoming reminders).",
        "Mirror the owner's language. Reply with a one-line confirmation.",
    ]
    return "\n".join(lines)


def run_daily_digest() -> str:
    logger.info("Running daily digest...")
    body = _build_input(
        period_label="daily (today)",
        records=read_decisions_for_day(datetime.now()),
        lookahead_days=7,
        subject=config.digest_email_subject,
        email_to=config.digest_email_to,
    )
    summary = asyncio.run(run_digest(body))
    logger.info("Daily digest done: %s", summary)
    return summary


def run_weekly_digest() -> str:
    logger.info("Running weekly digest...")
    body = _build_input(
        period_label="weekly (past 7 days)",
        records=read_decisions_since(7),
        lookahead_days=14,
        subject=config.weekly_digest_subject,
        email_to=config.weekly_digest_email_to,
    )
    summary = asyncio.run(run_digest(body))
    logger.info("Weekly digest done: %s", summary)
    return summary


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)
    if len(sys.argv) > 1 and sys.argv[1].lower().startswith("week"):
        print(run_weekly_digest())
    else:
        print(run_daily_digest())
