# Slack assistant — real-time, local-transcription, self-improving

A persistent **Slack Socket Mode** listener that replaces the old ~10-minute poll.
The instant you post in your inbox channel it transcribes any voice memo **locally**
(tuned for Chinese + English + code-switching), routes the request through the
**Claude Agent SDK** (Calendar / Gmail / compose-a-text / notes), and replies
in-thread with what it did. It also emails you an **end-of-day digest** and runs an
**hourly health check** that emails you if something breaks.

## Why this exists

The previous workflow woke Claude every ~10 minutes just to *check* Slack — wasting
tokens and adding up to 10 minutes of latency. This is event-driven: Claude only runs
when there's real work (a message arrives, or the one daily digest), and feedback is
instant via Slack reactions + threaded replies.

## Architecture

| File | Role |
|------|------|
| `listener.py` | Socket Mode app; filters messages, transcribes, runs the agent, posts feedback |
| `transcription.py` | Local STT: `sensevoice` (default) / `paraformer` / `faster-whisper` |
| `agent.py` | Claude Agent SDK turn; wires MCP servers; reads/updates `memory.md` |
| `digest.py` | End-of-day summary → prioritized → emailed to you |
| `healthcheck.py` | Hourly probe; emails an alert on failure |
| `scheduler.py` | In-process APScheduler for the digest + health check |
| `decisions.py` | Append-only activity log the digest reads |
| `routing_prompt.md` / `digest_prompt.md` | System prompts |
| `memory.md` | Durable preferences/learnings (the "gets smarter" store) |

## Setup

### 1. Python deps
```bash
python -m venv .venv && . .venv/bin/activate
pip install -r slack_assistant/requirements.txt
```
The first transcription downloads the SenseVoice model (one-time).

### 2. Slack app
- Create a Slack app and **enable Socket Mode**.
- Generate an **app-level token** (`xapp-`) with `connections:write`.
- Bot token scopes: `channels:history`, `groups:history`, `im:history`, `files:read`,
  `chat:write`, `reactions:write`, `users:read`.
- Event subscriptions: `message.channels`, `message.groups`, `message.im`.
- **Inbox channel:** a bot **cannot read your personal self-DM**. Create a private
  channel (e.g. `#me-inbox`), invite the bot, and put its id in `INBOX_CHANNEL_ID`.
  (Or DM the bot directly and use that DM's channel id.)

### 3. MCP servers
Create `mcp_servers.json` (path set by `MCP_CONFIG_PATH`) with your Slack, Gmail and
Google Calendar MCP servers in Claude Agent SDK `mcp_servers` format:
```json
{
  "gmail":    {"command": "npx", "args": ["-y", "@your/gmail-mcp"],    "env": {"...": "..."}},
  "calendar": {"command": "npx", "args": ["-y", "@your/gcal-mcp"],     "env": {"...": "..."}},
  "slack":    {"command": "npx", "args": ["-y", "@your/slack-mcp"],    "env": {"...": "..."}}
}
```

### 4. Config
Copy `slack_assistant/.env.example` to `slack_assistant/.env` and fill it in.

## Run
```bash
./run_listener.sh
```
This starts the listener and the in-process scheduler (daily digest + hourly health
check). Send a message in your inbox channel to test.

### Choosing a transcription model
Set `TRANSCRIBE_PROVIDER`:
- `sensevoice` — **default**, best Chinese+English code-switching, CPU-friendly.
- `paraformer` — strongest for heavy Mandarin + English mixing.
- `faster-whisper` — broad-language fallback (downloads `large-v3`).

## Scheduling without the in-process scheduler
If you prefer external cron, omit APScheduler and run:
```cron
0 21 * * *  cd /path/to/repo && ./.venv/bin/python -m slack_assistant.digest
0 *  * * *  cd /path/to/repo && ./.venv/bin/python -m slack_assistant.healthcheck
```

## Always-on + watchdog
Run under systemd with `Restart=always`. The in-process health check can't detect a
fully crashed process, so systemd's restart (or a cron heartbeat) is the backstop.
Example unit:
```ini
[Service]
ExecStart=/path/to/repo/run_listener.sh
Restart=always
RestartSec=5
```

## Retiring the old poll
Disable the previous ~10-minute scheduled trigger so Claude is no longer invoked on a
timer. If it's a Claude-Code-on-web trigger, turn it off in the trigger settings.

## Tests
```bash
pip install pytest
pytest slack_assistant/tests -q
```
