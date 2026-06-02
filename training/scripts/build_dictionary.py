from pathlib import Path
import json
import pandas as pd

ROOT = Path(__file__).resolve().parents[2]

dataset_path = ROOT / "datasets" / "raw" / "Arabizi-Arabic Parallel corpora.xlsx"
processed_dir = ROOT / "datasets" / "processed"

processed_dir.mkdir(parents=True, exist_ok=True)

df = pd.read_excel(dataset_path)

forward_dictionary = {}
reverse_dictionary = {}

for _, row in df.iterrows():

    arabizi = str(row["Arabize"]).strip().lower()
    arabic = str(row["Arabic"]).strip()

    if not arabizi or not arabic:
        continue

    # Keep first occurrence
    forward_dictionary.setdefault(arabizi, arabic)
    reverse_dictionary.setdefault(arabic, arabizi)

forward_path = processed_dir / "dictionary.json"
reverse_path = processed_dir / "reverse_dictionary.json"

with open(forward_path, "w", encoding="utf-8") as f:
    json.dump(
        forward_dictionary,
        f,
        ensure_ascii=False,
        indent=2
    )

with open(reverse_path, "w", encoding="utf-8") as f:
    json.dump(
        reverse_dictionary,
        f,
        ensure_ascii=False,
        indent=2
    )

print(f"Forward entries: {len(forward_dictionary):,}")
print(f"Reverse entries: {len(reverse_dictionary):,}")

print(f"Saved: {forward_path}")
print(f"Saved: {reverse_path}")