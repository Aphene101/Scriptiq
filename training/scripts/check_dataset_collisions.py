from pathlib import Path
import pandas as pd

TRAINING_ROOT = Path(__file__).resolve().parents[1]

df = pd.read_csv(
    TRAINING_ROOT /
    "datasets" /
    "arabic-english" /
    "generated.csv"
)

duplicates = (
    df.groupby("Arabic")
      .size()
      .reset_index(name="count")
)

duplicates = duplicates[
    duplicates["count"] > 1
]

print(
    "Collisions:",
    len(duplicates)
)

print(
    duplicates
        .sort_values(
            "count",
            ascending=False
        )
        .head(50)
)