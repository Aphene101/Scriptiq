from pathlib import Path
import sys
import torch
import pandas as pd

import argparse

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))


from configs import load_config

from models.char_vocab import CharVocab
from models.seq2seq import Encoder, Decoder, Seq2Seq
from models.transformer_seq2seq import TransformerSeq2Seq

parser = argparse.ArgumentParser()
parser.add_argument("model")
args = parser.parse_args()

config = load_config(args.model)

DEVICE = torch.device("cpu")

CHECKPOINT_DIR = config["checkpoint_dir"]

source_vocab = CharVocab()
source_vocab.load(
    CHECKPOINT_DIR / "source_vocab.json"
)

target_vocab = CharVocab()
target_vocab.load(
    CHECKPOINT_DIR / "target_vocab.json"
)

if config["architecture"] == "seq2seq":

    encoder = Encoder(
        source_vocab.size(),
        config["embedding_dim"],
        config["hidden_dim"]
    )

    decoder = Decoder(
        target_vocab.size(),
        config["embedding_dim"],
        config["hidden_dim"]
    )

    model = Seq2Seq(
        encoder,
        decoder,
        target_vocab.size()
    )

elif config["architecture"] == "transformer":

    model = TransformerSeq2Seq(
        source_vocab_size=
            source_vocab.size(),
        target_vocab_size=
            target_vocab.size(),
        embedding_dim=
            config["embedding_dim"],
        max_length=max(
            config["max_source_length"],
            config["max_target_length"]
        )
    )

else:

    raise ValueError(
        f"Unknown architecture: "
        f"{config['architecture']}"
    )

model.load_state_dict(
    torch.load(
        CHECKPOINT_DIR / "model.pt",
        map_location=DEVICE
    )
)

model.eval()

test_df = pd.read_csv(
    config["dataset_dir"] /
    "test.csv"
)

correct_chars = 0
total_chars = 0

for _, row in test_df.iterrows():

    source_word = str(
        row[config["source_column"]]
    ).lower()

    expected = str(
        row[config["target_column"]]
    )

    encoded = source_vocab.encode(
        source_word
    )

    encoded = encoded[
        :config["max_source_length"]
    ]

    while len(encoded) < config["max_source_length"]:
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