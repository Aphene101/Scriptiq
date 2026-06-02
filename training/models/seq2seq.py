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
            decoder,
            target_vocab_size
    ):
        super().__init__()

        self.encoder = encoder
        self.decoder = decoder
        self.target_vocab_size = target_vocab_size

    def forward(
            self,
            source,
            target
    ):

        batch_size = source.shape[0]
        target_length = target.shape[1]

        outputs = torch.zeros(
            batch_size,
            target_length,
            self.target_vocab_size,
            device=source.device
        )

        hidden, cell = self.encoder(source)

        decoder_input = target[:, 0]

        for t in range(1, target_length):

            prediction, hidden, cell = self.decoder(
                decoder_input,
                hidden,
                cell
            )

            outputs[:, t] = prediction

            # Teacher forcing
            decoder_input = target[:, t]

        return outputs

    def predict(
            self,
            source,
            sos_token,
            eos_token,
            max_length=24
    ):

        self.eval()

        with torch.no_grad():

            hidden, cell = self.encoder(source)

            decoder_input = torch.tensor(
                [sos_token],
                device=source.device
            )

            result = []

            for _ in range(max_length):

                prediction, hidden, cell = self.decoder(
                    decoder_input,
                    hidden,
                    cell
                )

                predicted_token = prediction.argmax(1)

                token = predicted_token.item()

                if token == eos_token:
                    break

                result.append(token)

                decoder_input = predicted_token

            return result