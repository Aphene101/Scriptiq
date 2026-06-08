from pathlib import Path
import argparse

import pandas as pd
from sklearn.model_selection import train_test_split


ROOT = Path(__file__).resolve().parents[1]


parser = argparse.ArgumentParser()
parser.add_argument("model")
args = parser.parse_args()

dataset_dir = (
    ROOT /
    "datasets" /
    args.model
)

combined_path = (
    dataset_dir /
    "combined.csv"
)

generated_path = (
    dataset_dir /
    "generated.csv"
)

if combined_path.exists():

    df = pd.read_csv(
        combined_path
    )

else:

    df = pd.read_csv(
        generated_path
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