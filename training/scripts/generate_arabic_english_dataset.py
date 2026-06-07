from g2p_en import G2p
from pathlib import Path
import re

g2p = G2p()

PHONEME_MAP = {
    "AA": "ا",
    "AE": "ا",
    "AH": "ا",
    "AO": "و",
    "AW": "او",
    "AY": "اي",

    "B": "ب",
    "CH": "تش",
    "D": "د",
    "DH": "ذ",

    "EH": "ي",
    "ER": "ر",
    "EY": "ي",

    "F": "ف",
    "G": "ج",
    "HH": "ه",

    "IH": "ي",
    "IY": "ي",

    "JH": "ج",

    "K": "ك",
    "L": "ل",
    "M": "م",
    "N": "ن",

    "NG": "نج",

    "OW": "و",
    "OY": "وي",

    "P": "ب",

    "R": "ر",
    "S": "س",
    "SH": "ش",

    "T": "ت",
    "TH": "ث",

    "UH": "و",
    "UW": "و",

    "V": "ف",

    "W": "و",

    "Y": "ي",

    "Z": "ز",
    "ZH": "ج"
}

def english_to_arabic_spelling(word):

    phonemes = g2p(word)

    result = []

    for phoneme in phonemes:

        phoneme = phoneme.split("0")[0]
        phoneme = phoneme.split("1")[0]
        phoneme = phoneme.split("2")[0]

        mapped = PHONEME_MAP.get(
            phoneme
        )

        if mapped:
            result.append(mapped)

    return "".join(result)

from wordfreq import top_n_list

words = top_n_list(
    "en",
    50000
)

print(
    f"Loaded {len(words)} words"
)

filtered_words = []

for word in words:

    word = word.lower()

    if not word.isalpha():
        continue

    if len(word) < 3:
        continue

    filtered_words.append(word)

print(
    f"Filtered words: {len(filtered_words)}"
)

print(filtered_words[:20])

import pandas as pd

rows = []

for word in filtered_words:

    arabic = english_to_arabic_spelling(
        word
    )

    if len(arabic) < 2:
        continue

    if re.search(
            r"[a-zA-Z]",
            arabic
    ):
        continue

    rows.append({
        "Arabic": arabic,
        "English": word
    })

print(
    f"Generated rows: {len(rows)}"
)

for row in rows[:50]:

    print(
        row["Arabic"],
        "->",
        row["English"]
    )

PROJECT_ROOT = Path(__file__).resolve().parents[2]

seen = set()

deduplicated_rows = []

for row in rows:

    key = (
        row["Arabic"],
        row["English"]
    )

    if key in seen:
        continue

    seen.add(key)

    deduplicated_rows.append(
        row
    )

rows = deduplicated_rows

df = pd.DataFrame(rows)

df = df.drop_duplicates(
    subset=["Arabic"],
    keep="first"
)

print(
    f"Rows after deduplication: {len(df)}"
)

IMPORTANT_WORDS = {
    "facebook",
    "instagram",
    "youtube",
    "spotify",

    "music",
    "song",
    "songs",
    "album",
    "albums",
    "artist",
    "artists",
    "playlist",

    "baby",
    "love",
    "lover",
    "crush",
    "romance",

    "crazy",
    "cool",
    "fashion",
    "style",
    "party",

    "game",
    "games",
    "gaming",
    "gamer",
    "player",
    "players",

    "hello",
    "bye",
    "weekend",
}

augmented_rows = []

for _, row in df.iterrows():

    augmented_rows.append(
        row.to_dict()
    )

    if (
        row["English"]
            .lower()
        in IMPORTANT_WORDS
    ):

        for _ in range(100):

            augmented_rows.append(
                row.to_dict()
            )

df = pd.DataFrame(
    augmented_rows
)

print(
    f"Rows after oversampling: {len(df)}"
)

from sklearn.model_selection import train_test_split

train_df, test_df = train_test_split(
    df,
    test_size=0.1,
    random_state=42
)

train_df, validation_df = train_test_split(
    train_df,
    test_size=0.1,
    random_state=42
)

dataset_dir = (
    PROJECT_ROOT /
    "training" /
    "datasets" /
    "arabic-english"
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

output_path = (
    PROJECT_ROOT /
    "training" /
    "datasets" /
    "arabic-english" /
    "generated.csv"
)

df.to_csv(
    output_path,
    index=False
)

print(
    f"Saved to {output_path}"
)

print("Train:", len(train_df))
print("Validation:", len(validation_df))
print("Test:", len(test_df))