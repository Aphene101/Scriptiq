from pathlib import Path
import torch

from models.char_vocab import CharVocab
from models.seq2seq import Encoder, Decoder, Seq2Seq
from models.transformer_seq2seq import TransformerSeq2Seq


class ModelService:

    def __init__(self, config):

        self.device = torch.device("cpu")
        self.config = self._normalize_config(config)
        checkpoints_dir = self.config["checkpoint_dir"]

        self.source_vocab = CharVocab()
        self.target_vocab = CharVocab()

        self.source_vocab.load(
            checkpoints_dir / "source_vocab.json"
        )

        self.target_vocab.load(
            checkpoints_dir / "target_vocab.json"
        )

        if self.config["architecture"] == "seq2seq":

            encoder = Encoder(
                self.source_vocab.size(),
                self.config["embedding_dim"],
                self.config["hidden_dim"]
            )

            decoder = Decoder(
                self.target_vocab.size(),
                self.config["embedding_dim"],
                self.config["hidden_dim"]
            )

            self.model = Seq2Seq(
                encoder,
                decoder,
                self.target_vocab.size()
            )

        elif self.config["architecture"] == "transformer":

            self.model = TransformerSeq2Seq(
                source_vocab_size=
                    self.source_vocab.size(),
                target_vocab_size=
                    self.target_vocab.size(),
                embedding_dim=
                    self.config["embedding_dim"],
                max_length=max(
                    self.config["max_source_length"],
                    self.config["max_target_length"]
                )
            )

        else:

            raise ValueError(
                f"Unknown architecture: "
                f"{self.config['architecture']}"
            )

        self.model.load_state_dict(
            torch.load(
                checkpoints_dir / "model.pt",
                map_location=self.device
            )
        )

        self.model.eval()

    def transliterate(self, text):

        encoded = self.source_vocab.encode(
            text.lower()
        )

        encoded = encoded[:self.config["max_source_length"]]

        while len(encoded) < self.config["max_source_length"]:
            encoded.append(0)

        source_tensor = torch.tensor(
            [encoded],
            dtype=torch.long,
            device=self.device
        )

        prediction = self.model.predict(
            source_tensor,
            self.target_vocab.char_to_idx["<SOS>"],
            self.target_vocab.char_to_idx["<EOS>"]
        )

        return self.target_vocab.decode(
            prediction
        )

    def _normalize_config(self, config):
        if isinstance(config, dict):
            return config

        return {
            "checkpoint_dir": Path(config),
            "architecture": "seq2seq",
            "embedding_dim": 128,
            "hidden_dim": 256,
            "max_source_length": 24,
            "max_target_length": 24,
        }
