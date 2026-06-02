from pathlib import Path
import sys
import time

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.model_service import ModelService

service = ModelService(
    TRAINING_ROOT / "checkpoints"
)

words = [
    "7abibi",
    "so2al",
    "ezayak",
    "aroo7",
    "mabrooook"
]

iterations = 1000

start = time.time()

for _ in range(iterations):

    for word in words:
        service.transliterate(word)

end = time.time()

total_predictions = (
    iterations *
    len(words)
)

elapsed = end - start

print()
print("Predictions:", total_predictions)
print("Seconds:", elapsed)
print("Predictions/sec:", total_predictions / elapsed)
print("Ms per prediction:",
      (elapsed * 1000) / total_predictions)