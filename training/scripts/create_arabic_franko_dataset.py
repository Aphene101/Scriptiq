from pathlib import Path
import pandas as pd

ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

source = pd.read_csv(
    ROOT /
    "datasets" /
    "processed" /
    "training_pairs.csv"
)

print(source.columns.tolist())

reversed_df = pd.DataFrame({
    "Arabic": source["Arabic"],
    "Franko": source["Arabize"]
})

output_dir = (
    TRAINING_ROOT /
    "datasets" /
    "arabic-franko"
)

output_dir.mkdir(
    parents=True,
    exist_ok=True
)

reversed_df.to_csv(
    output_dir /
    "generated.csv",
    index=False
)

print(
    "Rows:",
    len(reversed_df)
)

print(
    "Saved:",
    output_dir / "generated.csv"
)