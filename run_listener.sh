#!/usr/bin/env bash
# Launch the Slack assistant listener.
# Config is loaded from slack_assistant/.env by python-dotenv (see config.py),
# so we do NOT source the .env here — that avoids shell-quoting issues with
# values containing spaces (e.g. DIGEST_EMAIL_SUBJECT="Day Summary").
set -euo pipefail

cd "$(dirname "$0")"

exec python -m slack_assistant.listener
