#!/usr/bin/env bash
# Launch the FastAPI voice receiver (iPhone -> Mac Mini path).
# Config is loaded from slack_assistant/.env by python-dotenv (see config.py).
# Host/port are read from the loaded config rather than sourcing the .env in the
# shell, which avoids quoting issues with values containing spaces.
set -euo pipefail

cd "$(dirname "$0")"

HOST="$(python -c 'from slack_assistant.config import config; print(config.voice_api_host)')"
PORT="$(python -c 'from slack_assistant.config import config; print(config.voice_api_port)')"

exec uvicorn slack_assistant.voice_api:app --host "$HOST" --port "$PORT"
