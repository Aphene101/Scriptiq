import onnx

model = onnx.load(
    "exports/model.onnx"
)

onnx.checker.check_model(
    model
)

print("Model is valid")
print("IR:", model.ir_version)

for opset in model.opset_import:
    print("Opset:", opset.version)