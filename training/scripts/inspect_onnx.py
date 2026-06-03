# scripts/inspect_onnx.py

import onnxruntime as ort

for model_name in [
    "encoder.onnx",
    "decoder.onnx"
]:

    print()
    print(model_name)

    session = ort.InferenceSession(
        f"exports/{model_name}"
    )

    print("INPUTS")

    for inp in session.get_inputs():
        print(
            inp.name,
            inp.shape,
            inp.type
        )

    print("OUTPUTS")

    for out in session.get_outputs():
        print(
            out.name,
            out.shape,
            out.type
        )