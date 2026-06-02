from pathlib import Path
import sys

import pandas as pd
import torch

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.char_vocab import CharVocab
from models.seq2seq import Encoder, Decoder, Seq2Seq


train_df = pd.read_csv(
    PROJECT_ROOT /
    "datasets" /
    "processed" /
    "train.csv"
)

source_vocab = CharVocab()
source_vocab.build(train_df["Arabize"])

target_vocab = CharVocab()
target_vocab.build(train_df["Arabic"])

encoder = Encoder(
    vocab_size=source_vocab.size(),
    embedding_dim=128,
    hidden_dim=256
)

decoder = Decoder(
    vocab_size=target_vocab.size(),
    embedding_dim=128,
    hidden_dim=256
)

model = Seq2Seq(
    encoder,
    decoder
)

print(model)

print()
print("Source vocab:", source_vocab.size())
print("Target vocab:", target_vocab.size())

print()
print("Model created successfully.")