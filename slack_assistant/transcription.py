"""Local, offline speech-to-text for voice memos.

No cloud API keys required. Optimised for Chinese + English + code-switching.

Providers (selected via ``TRANSCRIBE_PROVIDER``):
  - ``sensevoice``     FunASR SenseVoice-Small. Default. Best zh/en code-switching,
                       CPU-friendly, ~15x faster than Whisper.
  - ``paraformer``     FunASR Paraformer-zh. Strongest for heavy Mandarin + English.
  - ``faster-whisper`` faster-whisper large-v3. Broad-language fallback.

Models are loaded lazily and cached for the process lifetime, so the first
transcription pays the load cost and subsequent ones are fast.
"""

from __future__ import annotations

import functools
import logging

from .config import config

logger = logging.getLogger(__name__)


class TranscriptionError(RuntimeError):
    """Raised when transcription fails for any provider."""


# --- Provider implementations -------------------------------------------------


@functools.lru_cache(maxsize=1)
def _sensevoice_model():
    from funasr import AutoModel  # imported lazily so tests don't need funasr

    logger.info("Loading SenseVoice-Small model (first use)...")
    return AutoModel(
        model="iic/SenseVoiceSmall",
        vad_model="fsmn-vad",
        disable_update=True,
    )


def _transcribe_sensevoice(audio_path: str) -> str:
    from funasr.utils.postprocess_utils import rich_transcription_postprocess

    model = _sensevoice_model()
    # language="auto" lets the model detect zh/en/mixed per segment.
    result = model.generate(input=audio_path, language="auto", use_itn=True)
    if not result:
        return ""
    return rich_transcription_postprocess(result[0]["text"]).strip()


@functools.lru_cache(maxsize=1)
def _paraformer_model():
    from funasr import AutoModel

    logger.info("Loading Paraformer-zh model (first use)...")
    return AutoModel(
        model="paraformer-zh",
        vad_model="fsmn-vad",
        punc_model="ct-punc",
        disable_update=True,
    )


def _transcribe_paraformer(audio_path: str) -> str:
    model = _paraformer_model()
    result = model.generate(input=audio_path)
    if not result:
        return ""
    return str(result[0].get("text", "")).strip()


@functools.lru_cache(maxsize=1)
def _faster_whisper_model():
    from faster_whisper import WhisperModel

    logger.info("Loading faster-whisper large-v3 model (first use)...")
    return WhisperModel("large-v3", device="auto", compute_type="auto")


def _transcribe_faster_whisper(audio_path: str) -> str:
    model = _faster_whisper_model()
    # No language hint -> autodetect, which handles zh/en switching reasonably.
    segments, _info = model.transcribe(audio_path, vad_filter=True)
    return "".join(segment.text for segment in segments).strip()


_PROVIDERS = {
    "sensevoice": _transcribe_sensevoice,
    "paraformer": _transcribe_paraformer,
    "faster-whisper": _transcribe_faster_whisper,
}


# --- Public API ---------------------------------------------------------------


def transcribe(audio_path: str, provider: str | None = None) -> str:
    """Transcribe an audio file to text using the configured local provider.

    Args:
        audio_path: Path to a local audio file (wav/mp3/m4a/ogg...).
        provider: Override the configured provider. Defaults to
            ``config.transcribe_provider``.

    Returns:
        The transcribed text (possibly empty for silent audio).
    """
    name = (provider or config.transcribe_provider or "sensevoice").lower()
    impl = _PROVIDERS.get(name)
    if impl is None:
        raise TranscriptionError(
            f"Unknown TRANSCRIBE_PROVIDER {name!r}. "
            f"Valid options: {', '.join(sorted(_PROVIDERS))}."
        )
    try:
        return impl(audio_path)
    except TranscriptionError:
        raise
    except Exception as exc:  # noqa: BLE001 - surface a uniform error type
        raise TranscriptionError(f"{name} transcription failed: {exc}") from exc


def available_providers() -> list[str]:
    return sorted(_PROVIDERS)
