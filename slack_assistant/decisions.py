"""Append-only activity log so the daily digest can recap the day.

Each routed action is written as one JSON line to ``decisions.jsonl``.
"""

from __future__ import annotations

import json
from datetime import datetime, timedelta, timezone
from pathlib import Path

_PKG_DIR = Path(__file__).resolve().parent
DECISIONS_PATH = _PKG_DIR / "decisions.jsonl"


def log_decision(*, transcript: str, summary: str, ok: bool) -> None:
    record = {
        "ts": datetime.now(timezone.utc).isoformat(),
        "transcript": transcript,
        "summary": summary,
        "ok": ok,
    }
    with DECISIONS_PATH.open("a", encoding="utf-8") as fh:
        fh.write(json.dumps(record, ensure_ascii=False) + "\n")


def read_decisions_for_day(day: datetime | None = None) -> list[dict]:
    """Return the decision records logged on the given local calendar day."""
    if not DECISIONS_PATH.exists():
        return []
    day = (day or datetime.now()).date()
    out: list[dict] = []
    for line in DECISIONS_PATH.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line:
            continue
        try:
            rec = json.loads(line)
            rec_ts = datetime.fromisoformat(rec["ts"]).astimezone().date()
        except (json.JSONDecodeError, KeyError, ValueError):
            continue
        if rec_ts == day:
            out.append(rec)
    return out


def read_decisions_since(days: int) -> list[dict]:
    """Return decision records logged within the last ``days`` days."""
    if not DECISIONS_PATH.exists():
        return []
    cutoff = datetime.now(timezone.utc) - timedelta(days=days)
    out: list[dict] = []
    for line in DECISIONS_PATH.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line:
            continue
        try:
            rec = json.loads(line)
            rec_ts = datetime.fromisoformat(rec["ts"])
            if rec_ts.tzinfo is None:
                rec_ts = rec_ts.replace(tzinfo=timezone.utc)
        except (json.JSONDecodeError, KeyError, ValueError):
            continue
        if rec_ts >= cutoff:
            out.append(rec)
    return out
