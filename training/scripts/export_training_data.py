from pathlib import Path
import pandas as pd

ROOT = Path(__file__).resolve().parents[2]

dataset_path = (
    ROOT /
    "datasets" /
    "raw" /
    "Arabizi-Arabic Parallel corpora.xlsx"
)

df = pd.read_excel(dataset_path)

df = df[["Arabize", "Arabic"]]

df = df.dropna()

df["Arabize"] = df["Arabize"].astype(str).str.strip().str.lower()
df["Arabic"] = df["Arabic"].astype(str).str.strip()

df = df.drop_duplicates()

processed_dir = (
    ROOT /
    "datasets" /
    "processed"
)

processed_dir.mkdir(
    parents=True,
    exist_ok=True
)

output_path = (
    processed_dir /
    "training_pairs.csv"
)

df.to_csv(
    output_path,
    index=False,
    encoding="utf-8"
)

print(f"Rows: {len(df):,}")
print(output_path)