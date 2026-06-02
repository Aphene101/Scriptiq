from pathlib import Path
import sys

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.model_service import ModelService

service = ModelService(
    TRAINING_ROOT / "checkpoints"
)

while True:

    text = input("\nArabizi > ")

    print(
        "Arabic >",
        service.transliterate(text)
    )