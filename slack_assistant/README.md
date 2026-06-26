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

### 1b. Claude auth (use your Max/Pro subscription — no API key)
The agent runs on the Claude Code engine, so it can authenticate with your existing
**Max/Pro subscription** instead of a paid API key. On the Mac Mini:

```bash
# Install the Claude Code CLI (Node.js required)
npm install -g @anthropic-ai/claude-code

# RECOMMENDED (headless): mint a long-lived token from your subscription
claude setup-token
# → copy the token into CLAUDE_CODE_OAUTH_TOKEN in slack_assistant/.env
```
This uses your subscription (no per-call billing) and never needs interactive re-login,
which is ideal for an always-on service. Alternatively, just run `claude` once to log in
interactively (credentials saved on disk), or set `ANTHROPIC_API_KEY` for pay-as-you-go.
Pick exactly one; leave the others blank.

### 2. Slack app (text + image intake, and the output surface)
The bot both **receives** your typed text / images and **posts back** results (including
those from the iPhone voice path). Steps:

1. **Create the app** at api.slack.com/apps → *From scratch*. For privacy, create it in a
   **personal Slack workspace** (one you own), not a company workspace — company admins on
   Business+/Enterprise Grid can export DMs. Sign up with a **personal email**, since
   whoever controls the email can reset the account, and Enterprise Grid can auto-absorb
   workspaces created with a claimed company domain.
2. **Enable Socket Mode** → generate an **app-level token** (`xapp-…`) with `connections:write`.
3. **OAuth & Permissions → Bot Token Scopes:**
   - `channels:history`, `groups:history`, `im:history` — read messages in the inbox
   - `files:read` — download attached images (and any audio)
   - `chat:write` — post results / threaded replies (this is the **output** surface)
   - `reactions:write` — the 👀 → ✅/❌ feedback
   - `users:read` — identify the sender
   Install to the workspace and copy the **bot token** (`xoxb-…`).
4. **Event Subscriptions → Subscribe to bot events:** `message.channels`, `message.groups`,
   `message.im`. (These deliver text *and* file-share events, so images arrive automatically
   — no extra scope beyond `files:read` to fetch them.)
5. **Inbox channel:** a bot **cannot read your personal self-DM**. Use **Option A — DM the
   bot**: open a direct message with the bot app and send text/images there; use that DM's
   conversation id (`D…`) as `INBOX_CHANNEL_ID`. (Or make a private channel and invite the
   bot, using its `C…` id.)
6. **Output channel:** results post back to `SLACK_OUTPUT_CHANNEL`; leave it blank to reuse
   `INBOX_CHANNEL_ID` (the same DM/channel), so everything stays in one place.

**What flows through Slack:** typed **text** and **images** are processed by Claude (images
are read multimodally). **Voice** does *not* go through Slack — it comes via the iPhone path
below — but its transcript + result are **posted to Slack** as the log surface.

### 3. MCP servers (Gmail + Google Calendar)
The agent reaches Gmail and Calendar through one MCP server:
[`taylorwilsdon/google_workspace_mcp`](https://github.com/taylorwilsdon/google_workspace_mcp)
(PyPI `workspace-mcp`, run via `uvx`). One Google login covers both.

**a. Google OAuth credentials (one-time):**
1. Go to [console.cloud.google.com](https://console.cloud.google.com) → create a project.
2. **APIs & Services → Library** → enable **Gmail API** and **Google Calendar API**.
3. **OAuth consent screen** → User type **External** → fill app name/email → add your
   Gmail as a **Test user**.
4. **Credentials → Create Credentials → OAuth client ID** → Application type
   **Desktop app** → copy the **Client ID** and **Client secret**.

**b. Install the runtime** (provides `uvx`):
```bash
brew install uv     # macOS
```

**c. Create `mcp_servers.json`** (gitignored) from the template and fill in the secrets:
```bash
cp mcp_servers.example.json mcp_servers.json
nano mcp_servers.json      # paste Client ID + Client secret
```
The file (Claude Agent SDK `mcp_servers` format — a map of server name → stdio config):
```json
{
  "google_workspace": {
    "type": "stdio",
    "command": "uvx",
    "args": ["workspace-mcp", "--tools", "gmail", "calendar"],
    "env": {
      "GOOGLE_OAUTH_CLIENT_ID": "…",
      "GOOGLE_OAUTH_CLIENT_SECRET": "…",
      "OAUTHLIB_INSECURE_TRANSPORT": "1",
      "USER_GOOGLE_EMAIL": "lizy1630@gmail.com"
    }
  }
}
```
Ensure `MCP_CONFIG_PATH=./mcp_servers.json` in `.env` (the default).

**d. First-run authorization:** the first time you ask for a calendar event or email,
the server opens a browser for Google consent (callback on `localhost:8000`). Approve it
once — the token is cached, so later requests are silent. If that first request fails
while you're authorizing, just send it again.

### 4. Config
Copy `slack_assistant/.env.example` to `slack_assistant/.env` and fill it in.

## Run
```bash
./run_listener.sh      # Slack text/image intake + scheduler (digest + health check)
./run_voice_api.sh     # FastAPI voice receiver for the iPhone path (separate process)
```
`run_listener.sh` starts the Slack listener and the in-process scheduler. `run_voice_api.sh`
starts the `/voice` HTTP receiver. Run both on the Mac Mini (Phase 3 adds systemd/launchd
units so they stay up). Send a message in your inbox channel — or a voice note from your
phone — to test.

## iPhone voice path (Back Tap → Mac Mini → Whisper → Claude → Slack)

Capture a thought with a double-tap on the back of your phone; the audio goes straight to
the Mac Mini (not through Slack), is transcribed locally, routed by Claude, and the result
is posted to Slack.

### Receiver
The endpoint is `POST /voice` (see `voice_api.py`). It accepts the raw audio body (or a
multipart `file` field), transcribes via the configured local model, runs the agent, posts
`🎙️ <transcript> → <result>` to Slack, and returns `{ok, transcript, summary}`.

Set in `.env`:
- `VOICE_API_HOST` = the Mac's **Tailscale IP** (`100.x.y.z`) in production so the port is
  unreachable off-tailnet. Use `127.0.0.1` for local-only testing.
- `VOICE_API_PORT` = e.g. `8765`.

Smoke test locally:
```bash
curl --data-binary @sample.m4a -H "Content-Type: audio/m4a" \
  http://127.0.0.1:8765/voice
```

### iOS Shortcut
Create a Shortcut named e.g. "Voice → Mac" with two actions:
1. **Record Audio** — Start: *Immediately*, Stop: *On Tap* (records m4a).
2. **Get Contents of URL** —
   - URL: `http://<mac-tailscale-ip>:8765/voice`
   - Method: `POST`
   - Request Body: **File** → the *Recorded Audio* output
   - (Add header `Content-Type: audio/m4a` if Shortcuts doesn't set it.)
3. *(Optional)* **Show Notification** with the response's `summary`.

### Back Tap trigger
Settings → Accessibility → Touch → **Back Tap** → **Double Tap** → pick the Shortcut.
Double-tap, speak, tap the screen to stop.

### Tailscale + security (Tailscale-only auth)
Install Tailscale on the iPhone and Mac Mini (same tailnet); use the Mac's Tailscale IP in
the URL. The endpoint has **no app-layer token** — it relies on the tailnet:
- Bind `VOICE_API_HOST` to the Tailscale IP (never `0.0.0.0` on public Wi-Fi).
- **Do not** use Tailscale Funnel or router port-forwarding — keep it tailnet-private
  (WireGuard already encrypts, so plain HTTP within the tailnet is fine).
- Optionally use Tailscale ACLs to allow only your iPhone to reach the port.
- The receiver still enforces `MAX_UPLOAD_MB`, an audio content-type allowlist, and temp-file
  cleanup. Residual risk: any device on your tailnet could POST `/voice` — acceptable for a
  single-user tailnet; add a shared secret / `tailscale serve` later if it grows.

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
