from pathlib import Path
import json
import re
from datasets import load_dataset

ROOT = Path(__file__).resolve().parents[2]

with open(
    ROOT / "datasets" / "processed" / "dictionary.json",
    encoding="utf-8"
) as f:
    dictionary = json.load(f)

dataset = load_dataset(
    "UBC-NLP/nilechat-arabizi-egy"
)

train = dataset["train"]

total_words = 0
matched_words = 0

for row in train.select(range(5000)):

    words = re.findall(
        r"[a-zA-Z0-9']+",
        row["text"].lower()
    )

    for word in words:

        total_words += 1

        if word in dictionary:
            matched_words += 1

coverage = (
    matched_words / total_words * 100
)

print(f"Words: {total_words:,}")
print(f"Matched: {matched_words:,}")
print(f"Coverage: {coverage:.2f}%")