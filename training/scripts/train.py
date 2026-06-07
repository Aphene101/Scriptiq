from pathlib import Path
import argparse
import sys

import pandas as pd
import torch
import torch.nn as nn
from torch.utils.data import DataLoader


TRAINING_ROOT = Path(__file__).resolve().parents[1]
sys.path.append(str(TRAINING_ROOT))

from configs import load_config
from models.char_dataset import CharDataset
from models.char_vocab import CharVocab
from models.seq2seq import Decoder, Encoder, Seq2Seq
from models.transformer_seq2seq import (
    TransformerSeq2Seq
)


def train(config):
    device = torch.device(
        "cuda" if torch.cuda.is_available() else "cpu"
    )

    train_df = pd.read_csv(
        config["dataset_dir"] / "train.csv"
    )

    source_vocab = CharVocab()
    source_vocab.build(train_df[config["source_column"]])

    target_vocab = CharVocab()
    target_vocab.build(train_df[config["target_column"]])

    dataset = CharDataset(
        train_df,
        source_vocab,
        target_vocab,
        source_column=config["source_column"],
        target_column=config["target_column"],
        max_source_length=config["max_source_length"],
        max_target_length=config["max_target_length"],
    )

    loader = DataLoader(
        dataset,
        batch_size=config["batch_size"],
        shuffle=True,
    )

    if config["architecture"] == "seq2seq":

        encoder = Encoder(
            source_vocab.size(),
            config["embedding_dim"],
            config["hidden_dim"],
        )

        decoder = Decoder(
            target_vocab.size(),
            config["embedding_dim"],
            config["hidden_dim"],
        )

        model = Seq2Seq(
            encoder,
            decoder,
            target_vocab.size(),
        ).to(device)

    elif config["architecture"] == "transformer":

        model = TransformerSeq2Seq(
            source_vocab_size=
                source_vocab.size(),
            target_vocab_size=
                target_vocab.size(),
            embedding_dim=
                config["embedding_dim"],
            max_length=max(
                config[
                    "max_source_length"
                ],
                config[
                    "max_target_length"
                ]
            )
        ).to(device)

    else:

        raise ValueError(
            f"Unknown architecture: {config['architecture']}"
        )

    criterion = nn.CrossEntropyLoss(
        ignore_index=0
    )

    optimizer = torch.optim.Adam(
        model.parameters(),
        lr=config["learning_rate"],
    )

    print("Model:", config["name"])
    print("Device:", device)
    print("Dataset size:", len(dataset))
    print("Batches:", len(loader))

    for epoch in range(config["epochs"]):
        model.train()
        total_loss = 0

        for batch_index, (source_batch, target_batch) in enumerate(loader):

            source_batch = source_batch.to(device)
            target_batch = target_batch.to(device)

            optimizer.zero_grad()

            if config["architecture"] == "transformer":

                decoder_input = target_batch[:, :-1]

                expected_output = target_batch[:, 1:]

                output = model(
                    source_batch,
                    decoder_input,
                )

                loss = criterion(
                    output.reshape(
                        -1,
                        target_vocab.size(),
                    ),
                    expected_output.reshape(-1),
                )

            else:

                output = model(
                    source_batch,
                    target_batch,
                )

                loss = criterion(
                    output[:, 1:].reshape(
                        -1,
                        target_vocab.size(),
                    ),
                    target_batch[:, 1:].reshape(-1),
                )

            loss.backward()
            optimizer.step()

            total_loss += loss.item()

        average_loss = total_loss / len(loader)

        print(
            f"Epoch {epoch + 1}/{config['epochs']}"
            f" - Loss: {average_loss:.4f}"
        )

    checkpoint_dir = config["checkpoint_dir"]
    checkpoint_dir.mkdir(
        parents=True,
        exist_ok=True,
    )

    torch.save(
        model.state_dict(),
        checkpoint_dir / "model.pt",
    )

    source_vocab.save(
        checkpoint_dir / "source_vocab.json",
    )

    target_vocab.save(
        checkpoint_dir / "target_vocab.json",
    )

    print("Model and vocabularies saved to", checkpoint_dir)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("model")
    args = parser.parse_args()

    train(load_config(args.model))


if __name__ == "__main__":
    main()
