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

# Load vocabularies
source_vocab = CharVocab()
source_vocab.load(
    CHECKPOINT_DIR / "source_vocab.json"
)

target_vocab = CharVocab()
target_vocab.load(
    CHECKPOINT_DIR / "target_vocab.json"
)

# Load model
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

# Load test set
test_df = pd.read_csv(
    PROJECT_ROOT /
    "datasets" /
    "processed" /
    "test.csv"
)

correct = 0
total = 0
wrong_examples = 0

for _, row in test_df.iterrows():

    source_word = str(row["Arabize"]).lower()
    expected = str(row["Arabic"])

    encoded = source_vocab.encode(source_word)

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

    predicted_word = target_vocab.decode(
        prediction
    )

    if predicted_word == expected:
        correct += 1
    else:

        if wrong_examples < 30:

            print()
            print("Arabizi :", source_word)
            print("Expected:", expected)
            print("Predicted:", predicted_word)

            wrong_examples += 1

    total += 1

accuracy = correct / total * 100

print()
print(f"Correct: {correct}")
print(f"Total: {total}")
print(f"Word Accuracy: {accuracy:.2f}%")