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

encoder = service.model.encoder

encoder.eval()

dummy_source = torch.randint(
    0,
    52,
    (1, 24),
    dtype=torch.long
)

exports_dir = TRAINING_ROOT / "exports"

torch.onnx.export(
    encoder,
    dummy_source,
    exports_dir / "encoder.onnx",
    input_names=["source"],
    output_names=["hidden", "cell"],
    opset_version=17
)

print("Encoder exported")