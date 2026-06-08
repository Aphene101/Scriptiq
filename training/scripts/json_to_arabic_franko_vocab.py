from pathlib import Path
import json
import pandas as pd

TRAINING_ROOT = Path(__file__).resolve().parents[1]
ROOT = Path(__file__).resolve().parents[2]

with open(
    ROOT / "datasets" / "processed" / "franko_arabic_approved_words.json",
    encoding="utf-8"
) as f:
    data = json.load(f)

rows = []

for franko, arabic in data.items():

    rows.append({
        "Arabic": arabic,
        "Franko": franko
    })

df = pd.DataFrame(rows)

df.to_csv(
    TRAINING_ROOT /
    "datasets" /
    "arabic-franko" /
    "high_value_vocab.csv",
    index=False
)

print(len(df))