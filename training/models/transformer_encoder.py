import torch
import torch.nn as nn


class TransformerEncoderModel(
        nn.Module
):

    def __init__(
            self,
            source_vocab_size,
            target_vocab_size,
            embedding_dim=256,
            num_heads=8,
            num_layers=4,
            max_length=32,
            dropout=0.1
    ):

        super().__init__()

        self.embedding = nn.Embedding(
            source_vocab_size,
            embedding_dim
        )

        self.position_embedding = nn.Embedding(
            max_length,
            embedding_dim
        )

        encoder_layer = nn.TransformerEncoderLayer(
            d_model=embedding_dim,
            nhead=num_heads,
            dropout=dropout,
            batch_first=True
        )

        self.encoder = nn.TransformerEncoder(
            encoder_layer,
            num_layers=num_layers
        )

        self.output = nn.Linear(
            embedding_dim,
            target_vocab_size
        )

    def forward(
            self,
            source
    ):

        batch_size, sequence_length = source.shape

        positions = torch.arange(
            sequence_length,
            device=source.device
        )

        positions = positions.unsqueeze(0)

        positions = positions.expand(
            batch_size,
            sequence_length
        )

        x = (
            self.embedding(source)
            +
            self.position_embedding(
                positions
            )
        )

        x = self.encoder(x)

        x = self.output(x)

        return x