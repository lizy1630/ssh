#!/usr/bin/env bash
# Launch the FastAPI voice receiver (iPhone -> Mac Mini path). Loads .env if present.
set -euo pipefail

cd "$(dirname "$0")"

if [ -f slack_assistant/.env ]; then
  set -a
  # shellcheck disable=SC1091
  . slack_assistant/.env
  set +a
fi

HOST="${VOICE_API_HOST:-127.0.0.1}"
PORT="${VOICE_API_PORT:-8765}"

exec uvicorn slack_assistant.voice_api:app --host "$HOST" --port "$PORT"
