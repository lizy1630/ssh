"""Claude Agent SDK runner — the brain that routes a request to an action.

Given a transcript (from text or a transcribed voice memo), the agent decides
the intent and executes it through MCP tools (Slack / Gmail / Google Calendar).
It reads ``memory.md`` for accumulated preferences so behaviour improves over
time, and appends durable learnings back to it.

MCP servers are configured out-of-band via a JSON file (``MCP_CONFIG_PATH``)
in the Claude Agent SDK ``mcp_servers`` format, e.g.::

    {
      "slack":    {"command": "npx", "args": ["-y", "@modelcontextprotocol/server-slack"], "env": {...}},
      "gmail":    {"command": "...", "args": [...], "env": {...}},
      "calendar": {"command": "...", "args": [...], "env": {...}}
    }
"""

from __future__ import annotations

import json
import logging
import os
from pathlib import Path

from .config import config

logger = logging.getLogger(__name__)

_PKG_DIR = Path(__file__).resolve().parent
MEMORY_PATH = _PKG_DIR / "memory.md"
ROUTING_PROMPT_PATH = _PKG_DIR / "routing_prompt.md"
DIGEST_PROMPT_PATH = _PKG_DIR / "digest_prompt.md"


def _load_text(path: Path, default: str = "") -> str:
    try:
        return path.read_text(encoding="utf-8")
    except FileNotFoundError:
        return default


def load_mcp_servers() -> dict:
    """Load MCP server definitions from MCP_CONFIG_PATH (JSON). Empty if unset."""
    path = os.environ.get("MCP_CONFIG_PATH")
    if not path:
        logger.warning("MCP_CONFIG_PATH not set; agent will run without MCP tools.")
        return {}
    try:
        return json.loads(Path(path).read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        logger.error("Failed to read MCP config %s: %s", path, exc)
        return {}


def append_memory(note: str) -> None:
    """Append a durable learning to memory.md (used by the feedback loop)."""
    note = note.strip()
    if not note:
        return
    with MEMORY_PATH.open("a", encoding="utf-8") as fh:
        fh.write(f"\n- {note}\n")


def _build_options(system_prompt: str):
    from claude_agent_sdk import ClaudeAgentOptions

    return ClaudeAgentOptions(
        system_prompt=system_prompt,
        mcp_servers=load_mcp_servers(),
        # Act on the owner's behalf without interactive approval prompts.
        permission_mode="bypassPermissions",
        # Leave allowed_tools unset so BOTH the built-in Read tool (for viewing
        # attached images) and the MCP tools (mcp__<server>__*, e.g. Gmail /
        # Calendar) are available. Restricting it here would block MCP actions.
    )


async def _run_query(prompt: str, system_prompt: str) -> str:
    """Run a single Agent SDK turn and return the final assistant text."""
    from claude_agent_sdk import AssistantMessage, ResultMessage, TextBlock, query

    options = _build_options(system_prompt)
    final_text: list[str] = []
    async for message in query(prompt=prompt, options=options):
        if isinstance(message, AssistantMessage):
            for block in message.content:
                if isinstance(block, TextBlock):
                    final_text.append(block.text)
        elif isinstance(message, ResultMessage) and getattr(message, "result", None):
            final_text = [message.result]
    return "\n".join(t for t in final_text if t).strip()


async def run(
    transcript: str,
    context: dict | None = None,
    attachments: list[str] | None = None,
) -> str:
    """Route and execute a single user request. Returns a short summary string.

    Args:
        transcript: The text request (typed, or transcribed from a voice memo).
        context: Optional metadata, e.g. {"source": "slack" | "iphone-voice"}.
        attachments: Optional local image file paths. They are referenced in the
            prompt and the agent views them with its built-in Read tool. The caller
            owns the files and must keep them on disk until this coroutine returns.
    """
    system_prompt = _load_text(ROUTING_PROMPT_PATH)
    memory = _load_text(MEMORY_PATH)
    context = context or {}
    attachment_block = ""
    if attachments:
        listed = "\n".join(f"- {p}" for p in attachments)
        attachment_block = (
            "\n# Attached image(s)\n"
            "The request includes the following local image file(s). Use your Read "
            "tool to view each one before deciding the intent:\n"
            f"{listed}\n"
        )
    prompt = (
        f"# Your accumulated memory / preferences\n{memory}\n\n"
        f"# Incoming request (from Slack {context.get('source', 'message')})\n"
        f"{transcript}\n"
        f"{attachment_block}\n"
        "Decide the intent and execute it using your MCP tools. "
        "Then reply with a one-line summary of exactly what you did."
    )
    return await _run_query(prompt, system_prompt)


async def run_digest(digest_input: str) -> str:
    """Run the end-of-day digest turn (summarize, prioritize, email)."""
    system_prompt = _load_text(DIGEST_PROMPT_PATH)
    return await _run_query(digest_input, system_prompt)
