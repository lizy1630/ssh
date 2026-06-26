"""One-time Google OAuth bootstrap for the workspace MCP server.

Single-user `workspace-mcp` only starts its OAuth flow when an auth tool is
*called* — just running it in a terminal never opens the browser. This helper
launches the server (using the exact command/env from mcp_servers.json),
invokes its auth tool, and KEEPS the connection open so the localhost:8000
callback can complete. The resulting token is cached to
~/.google_workspace_mcp/credentials and reused by the assistant afterwards.

Run once on the Mac Mini:
    python -m slack_assistant.google_auth
"""

from __future__ import annotations

import asyncio
import os
import sys
import time
from pathlib import Path

from .agent import load_mcp_servers
from .config import config

CRED_DIR = Path.home() / ".google_workspace_mcp" / "credentials"


def _server_params():
    from mcp import StdioServerParameters

    servers = load_mcp_servers()
    if "google_workspace" not in servers:
        print("ERROR: 'google_workspace' not found in mcp_servers.json "
              "(MCP_CONFIG_PATH=%s)." % os.environ.get("MCP_CONFIG_PATH"))
        sys.exit(1)
    cfg = servers["google_workspace"]
    # Merge parent env (PATH for uvx, etc.) with the server's configured env.
    env = {**os.environ, **(cfg.get("env") or {})}
    return StdioServerParameters(
        command=cfg["command"], args=cfg.get("args", []), env=env
    )


def _existing_creds() -> set[str]:
    if not CRED_DIR.exists():
        return set()
    return {p.name for p in CRED_DIR.iterdir()}


async def _run() -> int:
    from mcp import ClientSession
    from mcp.client.stdio import stdio_client

    email = (
        config.digest_email_to  # falls back to the same Gmail you use elsewhere
        if not os.environ.get("USER_GOOGLE_EMAIL")
        else os.environ["USER_GOOGLE_EMAIL"]
    )
    params = _server_params()
    # Prefer the email configured for the MCP server, if present.
    email = (params.env or {}).get("USER_GOOGLE_EMAIL", email)

    before = _existing_creds()
    print(f"Authorizing Google for: {email}")
    print("A browser window should open. If it doesn't, copy the URL printed below.\n")

    async with stdio_client(params) as (read, write):
        async with ClientSession(read, write) as session:
            await session.initialize()
            tools = await session.list_tools()
            names = [t.name for t in tools.tools]
            auth_tool = next(
                (n for n in names if "auth" in n.lower() and "start" in n.lower()),
                next((n for n in names if "auth" in n.lower()), None),
            )
            if not auth_tool:
                print("ERROR: no auth tool found. Available tools:", names)
                return 1

            tool_obj = next(t for t in tools.tools if t.name == auth_tool)
            schema = getattr(tool_obj, "inputSchema", None) or {}
            props = schema.get("properties", {}) or {}
            print(f"Calling auth tool: {auth_tool}  (params: {list(props)})")

            # Build the argument set from the tool's own schema.
            def build_args(service_value: str | None) -> dict:
                a: dict = {}
                if "user_google_email" in props:
                    a["user_google_email"] = email
                if "service_name" in props:
                    enum = props["service_name"].get("enum")
                    a["service_name"] = enum[0] if enum else service_value
                # Fill any other required string args we don't know with a guess.
                for req in schema.get("required", []):
                    if req not in a:
                        a[req] = service_value or "calendar"
                return a

            # Try the schema enum first, then fall back across common labels.
            candidates = [None, "calendar", "Google Calendar", "gmail", "Gmail"]
            result = None
            last_err = None
            for cand in candidates:
                try:
                    r = await session.call_tool(auth_tool, build_args(cand))
                except Exception as exc:  # noqa: BLE001
                    last_err = exc
                    continue
                # A bad-argument error may come back as an error result, not a raise.
                if getattr(r, "isError", False):
                    last_err = "".join(
                        getattr(b, "text", "") for b in (r.content or [])
                    )
                    continue
                result = r
                break
            if result is None:
                print("ERROR calling auth tool:", last_err)
                return 1

            for block in getattr(result, "content", []) or []:
                text = getattr(block, "text", None)
                if text:
                    print(text)

            # Keep the session (and thus the server + :8000 listener) alive while
            # you complete consent in the browser. Poll for a new credentials file.
            print("\nWaiting for you to finish consent in the browser "
                  "(up to 5 minutes)...")
            for _ in range(150):
                new = _existing_creds() - before
                if new:
                    print(f"\n✅ Credentials saved: {', '.join(sorted(new))}")
                    print(f"   in {CRED_DIR}")
                    return 0
                await asyncio.sleep(2)

    print("\n⚠️  Timed out waiting for credentials. If you completed consent, "
          f"check {CRED_DIR}. Otherwise re-run this command.")
    return 1


def main() -> None:
    raise SystemExit(asyncio.run(_run()))


if __name__ == "__main__":
    main()
