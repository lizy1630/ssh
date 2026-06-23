#!/usr/bin/env bash
# Launch the Slack assistant listener (loads .env if present).
set -euo pipefail

cd "$(dirname "$0")"

if [ -f slack_assistant/.env ]; then
  set -a
  # shellcheck disable=SC1091
  . slack_assistant/.env
  set +a
fi

exec python -m slack_assistant.listener
