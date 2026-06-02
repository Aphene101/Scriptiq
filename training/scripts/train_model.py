from pathlib import Path
import sys

import pandas as pd
import torch
import torch.nn as nn

from torch.utils.data import DataLoader

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.char_vocab import CharVocab
from models.char_dataset import CharDataset
from models.seq2seq import Encoder, Decoder, Seq2Seq

DEVICE = torch.device(
    "cuda" if torch.cuda.is_available() else "cpu"
)

print("Device:", DEVICE)

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

dataset = CharDataset(
    train_df,
    source_vocab,
    target_vocab
)

loader = DataLoader(
    dataset,
    batch_size=64,
    shuffle=True
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
).to(DEVICE)

criterion = nn.CrossEntropyLoss(
    ignore_index=0
)

optimizer = torch.optim.Adam(
    model.parameters(),
    lr=0.001
)

print()
print("Dataset size:", len(dataset))
print("Batches:", len(loader))
print("Model ready.")

EPOCHS = 10

for epoch in range(EPOCHS):

    model.train()

    total_loss = 0

    for source_batch, target_batch in loader:

        source_batch = source_batch.to(DEVICE)
        target_batch = target_batch.to(DEVICE)

        optimizer.zero_grad()

        output = model(
            source_batch,
            target_batch
        )

        loss = criterion(
            output[:, 1:].reshape(
                -1,
                target_vocab.size()
            ),
            target_batch[:, 1:].reshape(-1)
        )

        loss.backward()

        optimizer.step()

        total_loss += loss.item()

    average_loss = total_loss / len(loader)

    print(
        f"Epoch {epoch + 1}/{EPOCHS}"
        f" - Loss: {average_loss:.4f}"
    )

CHECKPOINT_DIR = (
    TRAINING_ROOT / "checkpoints"
)

CHECKPOINT_DIR.mkdir(
    parents=True,
    exist_ok=True
)

torch.save(
    model.state_dict(),
    CHECKPOINT_DIR / "model.pt"
)

source_vocab.save(
    CHECKPOINT_DIR / "source_vocab.json"
)

target_vocab.save(
    CHECKPOINT_DIR / "target_vocab.json"
)

print()
print("Model and vocabularies saved.")