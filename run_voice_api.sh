#!/usr/bin/env bash
# Launch the FastAPI voice receiver (iPhone -> Mac Mini path).
# Config is loaded from slack_assistant/.env by python-dotenv (see config.py).
#
# Set VOICE_API_HOST=auto in .env to auto-bind to this machine's Tailscale IP
# (so your iPhone can reach it). Otherwise the configured host is used verbatim.
set -euo pipefail

cd "$(dirname "$0")"

HOST="$(python -c 'from slack_assistant.config import config; print(config.voice_api_host)')"
PORT="$(python -c 'from slack_assistant.config import config; print(config.voice_api_port)')"

if [ "$HOST" = "auto" ] || [ -z "$HOST" ]; then
  HOST="$(tailscale ip -4 2>/dev/null | head -n1 || true)"
  if [ -z "$HOST" ]; then
    echo "VOICE_API_HOST=auto but could not get a Tailscale IP; falling back to 0.0.0.0"
    HOST="0.0.0.0"
  else
    echo "Auto-detected Tailscale IP: $HOST"
  fi
fi

echo "Voice API listening on http://$HOST:$PORT  (POST /voice, GET /health)"
exec uvicorn slack_assistant.voice_api:app --host "$HOST" --port "$PORT"
