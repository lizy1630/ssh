"""End-of-day digest: summarize the day, prioritize, and email it.

Runs once daily. Feeds the day's logged activity to the Claude agent, which
also reads the calendar (today's additions + upcoming reminders) via MCP and
emails a prioritized recap to you with a fixed subject.
"""

from __future__ import annotations

import asyncio
import logging
from datetime import datetime

from .agent import run_digest
from .config import config
from .decisions import read_decisions_for_day

logger = logging.getLogger(__name__)


def _build_digest_input() -> str:
    today = datetime.now()
    records = read_decisions_for_day(today)
    lines = [
        f"Today is {today:%A, %Y-%m-%d}.",
        "",
        "Here is everything the assistant handled today (raw activity log):",
    ]
    if records:
        for r in records:
            status = "ok" if r.get("ok") else "FAILED"
            lines.append(f"- [{status}] {r.get('summary') or r.get('transcript')}")
    else:
        lines.append("- (no logged assistant activity today)")
    lines += [
        "",
        "Now produce the end-of-day digest:",
        "1. Read today's calendar additions and ALL upcoming events/reminders "
        "via your Calendar MCP tools.",
        "2. Prioritize today's notes/tasks into a clear ranked order, "
        "highlighting anything time-sensitive or already on the calendar.",
        "3. List upcoming reminders in chronological order so nothing is forgotten.",
        f"4. Email the result to {config.digest_email_to} with the exact subject "
        f'"{config.digest_email_subject}" using your Gmail MCP tools.',
        "Reply with a one-line confirmation of what you sent.",
    ]
    return "\n".join(lines)


def run_daily_digest() -> str:
    logger.info("Running end-of-day digest...")
    summary = asyncio.run(run_digest(_build_digest_input()))
    logger.info("Digest complete: %s", summary)
    return summary


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)
    print(run_daily_digest())
