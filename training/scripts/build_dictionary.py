from pathlib import Path
import pandas as pd
import json

ROOT = Path(__file__).resolve().parents[2]

dataset_path = ROOT / "datasets" / "raw" / "Arabizi-Arabic Parallel corpora.xlsx"

print(dataset_path)

df = pd.read_excel(dataset_path)

dictionary = {}

for _, row in df.iterrows():
    arabizi = str(row["Arabize"]).strip().lower()
    arabic = str(row["Arabic"]).strip()

    if arabizi and arabic:
        dictionary[arabizi] = arabic

print(f"Entries: {len(dictionary):,}")

with open(
    "../datasets/processed/dictionary.json",
    "w",
    encoding="utf-8"
) as f:
    json.dump(
        dictionary,
        f,
        ensure_ascii=False,
        indent=2
    )

print("Dictionary saved.")