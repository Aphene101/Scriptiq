from pathlib import Path
import onnxruntime as ort
import numpy as np

TRAINING_ROOT = Path(__file__).resolve().parents[1]

session = ort.InferenceSession(
    str(
        TRAINING_ROOT /
        "exports" /
        "model.onnx"
    )
)

print("Inputs:")

for inp in session.get_inputs():
    print(inp.name, inp.shape)

print()

print("Outputs:")

for out in session.get_outputs():
    print(out.name, out.shape)