import torch
import torch.nn as nn


class Encoder(nn.Module):

    def __init__(
            self,
            vocab_size,
            embedding_dim,
            hidden_dim
    ):
        super().__init__()

        self.embedding = nn.Embedding(
            vocab_size,
            embedding_dim
        )

        self.lstm = nn.LSTM(
            embedding_dim,
            hidden_dim,
            batch_first=True
        )

    def forward(self, x):

        embedded = self.embedding(x)

        _, (hidden, cell) = self.lstm(
            embedded
        )

        return hidden, cell


class Decoder(nn.Module):

    def __init__(
            self,
            vocab_size,
            embedding_dim,
            hidden_dim
    ):
        super().__init__()

        self.embedding = nn.Embedding(
            vocab_size,
            embedding_dim
        )

        self.lstm = nn.LSTM(
            embedding_dim,
            hidden_dim,
            batch_first=True
        )

        self.fc = nn.Linear(
            hidden_dim,
            vocab_size
        )

    def forward(
            self,
            x,
            hidden,
            cell
    ):

        x = x.unsqueeze(1)

        embedded = self.embedding(x)

        output, (hidden, cell) = self.lstm(
            embedded,
            (hidden, cell)
        )

        prediction = self.fc(
            output.squeeze(1)
        )

        return prediction, hidden, cell


class Seq2Seq(nn.Module):

    def __init__(
            self,
            encoder,
            decoder
    ):
        super().__init__()

        self.encoder = encoder
        self.decoder = decoder