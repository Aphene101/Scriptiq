from pathlib import Path

import pandas as pd
from sklearn.model_selection import train_test_split


ROOT = Path(__file__).resolve().parents[1]

dataset_dir = (
    ROOT /
    "datasets" /
    "arabic-english"
)

df = pd.read_csv(
    dataset_dir /
    "combined.csv"
)

train_df, temp_df = train_test_split(
    df,
    test_size=0.2,
    random_state=42,
    shuffle=True
)

validation_df, test_df = train_test_split(
    temp_df,
    test_size=0.5,
    random_state=42,
    shuffle=True
)

train_df.to_csv(
    dataset_dir / "train.csv",
    index=False
)

validation_df.to_csv(
    dataset_dir / "validation.csv",
    index=False
)

test_df.to_csv(
    dataset_dir / "test.csv",
    index=False
)

print(
    "Train:",
    len(train_df)
)

print(
    "Validation:",
    len(validation_df)
)

print(
    "Test:",
    len(test_df)
)