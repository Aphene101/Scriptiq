from pathlib import Path
import sys
import torch
import pandas as pd

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.char_vocab import CharVocab
from models.seq2seq import Encoder, Decoder, Seq2Seq

DEVICE = torch.device("cpu")

CHECKPOINT_DIR = TRAINING_ROOT / "checkpoints"

source_vocab = CharVocab()
source_vocab.load(
    CHECKPOINT_DIR / "source_vocab.json"
)

target_vocab = CharVocab()
target_vocab.load(
    CHECKPOINT_DIR / "target_vocab.json"
)

encoder = Encoder(
    source_vocab.size(),
    128,
    256
)

decoder = Decoder(
    target_vocab.size(),
    128,
    256
)

model = Seq2Seq(
    encoder,
    decoder,
    target_vocab.size()
)

model.load_state_dict(
    torch.load(
        CHECKPOINT_DIR / "model.pt",
        map_location=DEVICE
    )
)

model.eval()

test_df = pd.read_csv(
    PROJECT_ROOT /
    "datasets" /
    "processed" /
    "test.csv"
)

correct_chars = 0
total_chars = 0

for _, row in test_df.iterrows():

    source_word = str(row["Arabize"]).lower()
    expected = str(row["Arabic"])

    encoded = source_vocab.encode(
        source_word
    )

    encoded = encoded[:24]

    while len(encoded) < 24:
        encoded.append(0)

    source_tensor = torch.tensor(
        [encoded],
        dtype=torch.long,
        device=DEVICE
    )

    prediction = model.predict(
        source_tensor,
        target_vocab.char_to_idx["<SOS>"],
        target_vocab.char_to_idx["<EOS>"]
    )

    predicted = target_vocab.decode(
        prediction
    )

    max_len = max(
        len(expected),
        len(predicted)
    )

    for i in range(max_len):

        expected_char = (
            expected[i]
            if i < len(expected)
            else None
        )

        predicted_char = (
            predicted[i]
            if i < len(predicted)
            else None
        )

        if expected_char == predicted_char:
            correct_chars += 1

        total_chars += 1

accuracy = (
    correct_chars /
    total_chars *
    100
)

print()
print(
    f"Character Accuracy: {accuracy:.2f}%"
)
print(
    f"Correct Characters: {correct_chars}"
)
print(
    f"Total Characters: {total_chars}"
)