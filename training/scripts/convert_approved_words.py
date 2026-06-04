from pathlib import Path
import json
import csv

PROJECT_ROOT = Path(__file__).resolve().parents[2]

approved_words_path = (
    PROJECT_ROOT
    / "data"
    / "approved_words.json"
)

output_path = (
    PROJECT_ROOT
    / "training"
    / "approved_words.csv"
)

with open(
        approved_words_path,
        "r",
        encoding="utf-8"
) as f:
    words = json.load(f)

with open(
        output_path,
        "w",
        encoding="utf-8",
        newline=""
) as f:

    writer = csv.writer(f)

    for franko, arabic in words.items():

        writer.writerow([
            franko,
            arabic
        ])

print(
    f"Exported {len(words)} words to "
    f"{output_path}"
)