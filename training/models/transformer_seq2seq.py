import torch
import torch.nn as nn


class PositionalEncoding(
        nn.Module
):

    def __init__(
            self,
            d_model,
            max_length=256
    ):

        super().__init__()

        pe = torch.zeros(
            max_length,
            d_model
        )

        position = torch.arange(
            0,
            max_length
        ).unsqueeze(1)

        div_term = torch.exp(
            torch.arange(
                0,
                d_model,
                2
            )
            *
            (
                -torch.log(
                    torch.tensor(
                        10000.0
                    )
                )
                /
                d_model
            )
        )

        pe[:, 0::2] = torch.sin(
            position * div_term
        )

        pe[:, 1::2] = torch.cos(
            position * div_term
        )

        self.register_buffer(
            "pe",
            pe.unsqueeze(0)
        )

    def forward(
            self,
            x
    ):

        return (
            x
            +
            self.pe[
                :,
                :x.size(1)
            ]
        )


class TransformerSeq2Seq(
        nn.Module
):

    def __init__(
            self,
            source_vocab_size,
            target_vocab_size,
            embedding_dim=64,
            num_heads=2,
            num_layers=1,
            dropout=0.1,
            max_length=64
    ):

        super().__init__()

        self.target_vocab_size = (
            target_vocab_size
        )

        self.source_embedding = (
            nn.Embedding(
                source_vocab_size,
                embedding_dim
            )
        )

        self.target_embedding = (
            nn.Embedding(
                target_vocab_size,
                embedding_dim
            )
        )

        self.position_encoding = (
            PositionalEncoding(
                embedding_dim,
                max_length
            )
        )

        self.transformer = (
            nn.Transformer(
                d_model=embedding_dim,
                nhead=num_heads,
                num_encoder_layers=num_layers,
                num_decoder_layers=num_layers,
                dropout=dropout,
                batch_first=True
            )
        )

        self.fc = nn.Linear(
            embedding_dim,
            target_vocab_size
        )

    def forward(
            self,
            source,
            target
    ):

        source_embedded = (
            self.position_encoding(
                self.source_embedding(
                    source
                )
            )
        )

        target_embedded = (
            self.position_encoding(
                self.target_embedding(
                    target
                )
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
            source_embedded,
            target_embedded,
            tgt_mask=target_mask
        )

        return self.fc(output)

    def predict(
            self,
            source,
            sos_token,
            eos_token,
            max_length=24
    ):

        self.eval()

        with torch.no_grad():

            generated = torch.tensor(
                [[sos_token]],
                device=source.device
            )

            for _ in range(
                    max_length
            ):

                output = self.forward(
                    source,
                    generated
                )

                next_token = (
                    output[:, -1]
                    .argmax(-1)
                    .unsqueeze(1)
                )

                token = (
                    next_token
                    .item()
                )

                if token == eos_token:
                    break

                generated = torch.cat(
                    [
                        generated,
                        next_token
                    ],
                    dim=1
                )

            return (
                generated
                .squeeze(0)
                .tolist()[1:]
            )