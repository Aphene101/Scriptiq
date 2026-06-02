from pathlib import Path
import pandas as pd
from sklearn.model_selection import train_test_split

ROOT = Path(__file__).resolve().parents[2]

input_file = (
    ROOT /
    "datasets" /
    "processed" /
    "training_pairs.csv"
)

df = pd.read_csv(input_file)

train_df, temp_df = train_test_split(
    df,
    test_size=0.2,
    random_state=42
)

val_df, test_df = train_test_split(
    temp_df,
    test_size=0.5,
    random_state=42
)

output_dir = (
    ROOT /
    "datasets" /
    "processed"
)

train_df.to_csv(
    output_dir / "train.csv",
    index=False
)

val_df.to_csv(
    output_dir / "val.csv",
    index=False
)

test_df.to_csv(
    output_dir / "test.csv",
    index=False
)

print(f"Train: {len(train_df):,}")
print(f"Validation: {len(val_df):,}")
print(f"Test: {len(test_df):,}")