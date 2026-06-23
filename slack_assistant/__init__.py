"""Real-time Slack assistant: Socket Mode listener + local transcription + agent.

Note: we deliberately do NOT ``from .config import config`` here. Doing so would
rebind the ``slack_assistant.config`` attribute from the submodule to the Config
instance, shadowing the module and breaking ``import slack_assistant.config``.
Import the singleton where needed via ``from .config import config``.
"""
