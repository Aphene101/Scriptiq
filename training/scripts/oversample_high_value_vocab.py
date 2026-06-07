import pandas as pd
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

generated_path = (
    ROOT /
    "datasets" /
    "arabic-english" /
    "generated.csv"
)

high_value_path = (
    ROOT /
    "datasets" /
    "arabic-english" /
    "high_value_vocab.csv"
)

generated = pd.read_csv(generated_path)
high_value = pd.read_csv(high_value_path)

oversampled = pd.concat(
    [generated] +
    [high_value] * 100,
    ignore_index=True
)

oversampled.to_csv(
    ROOT /
    "datasets" /
    "arabic-english" /
    "combined.csv",
    index=False
)

print(
    "Generated:",
    len(generated)
)

print(
    "High value:",
    len(high_value)
)

print(
    "Combined:",
    len(oversampled)
)