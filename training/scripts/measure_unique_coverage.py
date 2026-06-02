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

unique_words = set()

for row in train.select(range(10000)):

    words = re.findall(
        r"[a-zA-Z0-9']+",
        row["text"].lower()
    )

    unique_words.update(words)

matched = sum(
    1 for word in unique_words
    if word in dictionary
)

coverage = matched / len(unique_words) * 100

print(f"Unique words: {len(unique_words):,}")
print(f"Matched unique words: {matched:,}")
print(f"Coverage: {coverage:.2f}%")