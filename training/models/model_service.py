from pathlib import Path
import torch

from models.char_vocab import CharVocab
from models.seq2seq import Encoder, Decoder, Seq2Seq


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
            "embedding_dim": 128,
            "hidden_dim": 256,
            "max_source_length": 24,
        }
