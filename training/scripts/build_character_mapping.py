from pathlib import Path
import pandas as pd
from collections import Counter

ROOT = Path(__file__).resolve().parents[2]

df = pd.read_csv(
    ROOT / "datasets" / "processed" / "train.csv"
)

arabizi_chars = Counter()

for word in df["Arabize"]:

    for c in str(word):
        arabizi_chars[c] += 1

print("Arabizi Characters:")
print()

for char, count in arabizi_chars.most_common():
    print(f"{char}: {count}")