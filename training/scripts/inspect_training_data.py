import pandas as pd
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]

df = pd.read_csv(
    ROOT / "datasets" / "processed" / "train.csv"
)

print(df.head(20))

print()
print("Rows:", len(df))

print()
print("Longest Arabizi word:",
      df["Arabize"].str.len().max())

print("Longest Arabic word:",
      df["Arabic"].str.len().max())