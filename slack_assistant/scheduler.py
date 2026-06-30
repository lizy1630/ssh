"""Background schedule for the daily digest and hourly health check.

Uses APScheduler running inside the listener process. For deployments where you
prefer external cron, you can instead invoke ``python -m slack_assistant.digest``
and ``python -m slack_assistant.healthcheck`` from crontab and skip this.
"""

from __future__ import annotations

import logging

from .config import config

logger = logging.getLogger(__name__)


def start_background_jobs() -> None:
    try:
        from apscheduler.schedulers.background import BackgroundScheduler
    except ImportError:
        logger.warning(
            "APScheduler not installed; daily digest and health check disabled. "
            "Install apscheduler or schedule them via external cron."
        )
        return

    from .digest import run_daily_digest, run_weekly_digest
    from .healthcheck import run_health_check

    scheduler = BackgroundScheduler()
    scheduler.add_job(
        run_daily_digest,
        "cron",
        hour=config.digest_hour,
        minute=0,
        id="daily_digest",
        misfire_grace_time=3600,
    )
    scheduler.add_job(
        run_weekly_digest,
        "cron",
        day_of_week=config.weekly_digest_day,
        hour=config.weekly_digest_hour,
        minute=0,
        id="weekly_digest",
        misfire_grace_time=3600,
    )
    scheduler.add_job(
        run_health_check,
        "interval",
        hours=max(1, config.healthcheck_hour_interval),
        id="health_check",
        misfire_grace_time=600,
    )
    scheduler.start()
    logger.info(
        "Scheduled daily digest at %02d:00, weekly digest %s at %02d:00, "
        "health check every %dh",
        config.digest_hour,
        config.weekly_digest_day,
        config.weekly_digest_hour,
        config.healthcheck_hour_interval,
    )
