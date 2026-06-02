from pathlib import Path
import json
import re
from collections import Counter
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

missing = Counter()

for row in train.select(range(10000)):

    words = re.findall(
        r"[a-zA-Z0-9']+",
        row["text"].lower()
    )

    for word in words:

        if word not in dictionary:
            missing[word] += 1

for word, count in missing.most_common(100):
    print(f"{word}: {count}")