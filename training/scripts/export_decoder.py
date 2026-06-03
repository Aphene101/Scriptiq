from pathlib import Path
import sys
import torch

PROJECT_ROOT = Path(__file__).resolve().parents[2]
TRAINING_ROOT = Path(__file__).resolve().parents[1]

sys.path.append(str(TRAINING_ROOT))

from models.model_service import ModelService

service = ModelService(
    TRAINING_ROOT / "checkpoints"
)

decoder = service.model.decoder

decoder.eval()

dummy_token = torch.randint(
    0,
    42,
    (1,),
    dtype=torch.long
)

dummy_hidden = torch.randn(
    1,
    1,
    256
)

dummy_cell = torch.randn(
    1,
    1,
    256
)

exports_dir = TRAINING_ROOT / "exports"

torch.onnx.export(
    decoder,
    (
        dummy_token,
        dummy_hidden,
        dummy_cell
    ),
    exports_dir / "decoder.onnx",
    input_names=[
        "token",
        "hidden",
        "cell"
    ],
    output_names=[
        "prediction",
        "hidden_out",
        "cell_out"
    ],
    opset_version=17
)

print("Decoder exported")