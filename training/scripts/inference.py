from pathlib import Path
import sys
import torch

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.char_vocab import CharVocab
from models.seq2seq import Encoder, Decoder, Seq2Seq

DEVICE = torch.device("cpu")

CHECKPOINT_DIR = (
    TRAINING_ROOT / "checkpoints"
)

source_vocab = CharVocab()
source_vocab.load(
    CHECKPOINT_DIR / "source_vocab.json"
)

target_vocab = CharVocab()
target_vocab.load(
    CHECKPOINT_DIR / "target_vocab.json"
)

encoder = Encoder(
    source_vocab.size(),
    128,
    256
)

decoder = Decoder(
    target_vocab.size(),
    128,
    256
)

model = Seq2Seq(
    encoder,
    decoder,
    target_vocab.size()
)

model.load_state_dict(
    torch.load(
        CHECKPOINT_DIR / "model.pt",
        map_location=DEVICE
    )
)

model.eval()
model.to(DEVICE)

print("Model loaded successfully")
print()
print("Source vocab:", source_vocab.size())
print("Target vocab:", target_vocab.size())

while True:

    text = input("\nArabizi > ").strip().lower()

    encoded = source_vocab.encode(text)

    encoded = encoded[:24]

    while len(encoded) < 24:
        encoded.append(0)

    source_tensor = torch.tensor(
        [encoded],
        dtype=torch.long,
        device=DEVICE
    )

    prediction = model.predict(
        source_tensor,
        target_vocab.char_to_idx["<SOS>"],
        target_vocab.char_to_idx["<EOS>"]
    )

    arabic = target_vocab.decode(
        prediction
    )

    print("Arabic >", arabic)