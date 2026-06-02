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

model = service.model

model.eval()

dummy_source = torch.randint(
    0,
    52,
    (1, 24),
    dtype=torch.long
)

dummy_target = torch.randint(
    0,
    42,
    (1, 24),
    dtype=torch.long
)

exports_dir = (
    TRAINING_ROOT / "exports"
)

exports_dir.mkdir(
    parents=True,
    exist_ok=True
)

torch.onnx.export(
    model,
    (dummy_source, dummy_target),
    exports_dir / "model.onnx",
    export_params=True,
    opset_version=17,
    do_constant_folding=True,
    input_names=[
        "source",
        "target"
    ],
    output_names=[
        "output"
    ]
)

print()
print("ONNX export complete")
print(exports_dir / "model.onnx")