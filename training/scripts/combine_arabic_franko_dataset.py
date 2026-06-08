from pathlib import Path
import pandas as pd

ROOT = Path(__file__).resolve().parents[1]

generated = pd.read_csv(
    ROOT /
    "datasets" /
    "arabic-franko" /
    "generated.csv"
)

high_value = pd.read_csv(
    ROOT /
    "datasets" /
    "arabic-franko" /
    "high_value_vocab.csv"
)

combined = pd.concat(
    [generated] +
    [high_value] * 100,
    ignore_index=True
)

combined.to_csv(
    ROOT /
    "datasets" /
    "arabic-franko" /
    "combined.csv",
    index=False
)

print(
    "Generated:",
    len(generated)
)

print(
    "High Value:",
    len(high_value)
)

print(
    "Combined:",
    len(combined)
)