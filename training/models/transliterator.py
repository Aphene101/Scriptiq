import torch
import torch.nn as nn


class Transliterator(nn.Module):

    def __init__(
            self,
            source_vocab_size,
            target_vocab_size,
            embedding_dim=128,
            hidden_dim=256
    ):

        super().__init__()

        self.embedding = nn.Embedding(
            source_vocab_size,
            embedding_dim
        )

        self.encoder = nn.LSTM(
            embedding_dim,
            hidden_dim,
            batch_first=True
        )

        self.classifier = nn.Linear(
            hidden_dim,
            target_vocab_size
        )

    def forward(self, x):

        x = self.embedding(x)

        output, _ = self.encoder(x)

        return self.classifier(output)