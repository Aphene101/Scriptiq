import torch
import torch.nn as nn


class TransformerModel(
        nn.Module
):

    def __init__(
            self,
            source_vocab_size,
            target_vocab_size,
            embedding_dim=256,
            num_heads=8,
            num_layers=4,
            dropout=0.1,
            max_length=64
    ):

        super().__init__()

        self.embedding_dim = embedding_dim

        self.source_embedding = nn.Embedding(
            source_vocab_size,
            embedding_dim
        )

        self.target_embedding = nn.Embedding(
            target_vocab_size,
            embedding_dim
        )

        self.position_embedding = nn.Embedding(
            max_length,
            embedding_dim
        )

        self.transformer = nn.Transformer(
            d_model=embedding_dim,
            nhead=num_heads,
            num_encoder_layers=num_layers,
            num_decoder_layers=num_layers,
            dropout=dropout,
            batch_first=True
        )

        self.output_layer = nn.Linear(
            embedding_dim,
            target_vocab_size
        )

    def forward(
            self,
            source,
            target
    ):

        batch_size = source.size(0)

        source_positions = (
            torch.arange(
                source.size(1),
                device=source.device
            )
            .unsqueeze(0)
            .expand(batch_size, -1)
        )

        target_positions = (
            torch.arange(
                target.size(1),
                device=target.device
            )
            .unsqueeze(0)
            .expand(batch_size, -1)
        )

        source_embedding = (
            self.source_embedding(source)
            +
            self.position_embedding(
                source_positions
            )
        )

        target_embedding = (
            self.target_embedding(target)
            +
            self.position_embedding(
                target_positions
            )
        )

        target_mask = (
            nn.Transformer
            .generate_square_subsequent_mask(
                target.size(1)
            )
            .to(source.device)
        )

        output = self.transformer(
            source_embedding,
            target_embedding,
            tgt_mask=target_mask
        )

        return self.output_layer(
            output
        )