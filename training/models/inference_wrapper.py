import torch
import torch.nn as nn


class InferenceWrapper(nn.Module):

    def __init__(
            self,
            seq2seq,
            sos_token,
            eos_token,
            max_length=24
    ):
        super().__init__()

        self.seq2seq = seq2seq
        self.sos_token = sos_token
        self.eos_token = eos_token
        self.max_length = max_length

    def forward(self, source):

        hidden, cell = self.seq2seq.encoder(
            source
        )

        decoder_input = torch.full(
            (source.shape[0],),
            self.sos_token,
            dtype=torch.long,
            device=source.device
        )

        outputs = []

        for _ in range(self.max_length):

            prediction, hidden, cell = (
                self.seq2seq.decoder(
                    decoder_input,
                    hidden,
                    cell
                )
            )

            decoder_input = prediction.argmax(1)

            outputs.append(
                decoder_input
            )

        return torch.stack(
            outputs,
            dim=1
        )