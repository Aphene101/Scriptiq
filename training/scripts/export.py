from pathlib import Path
import argparse
import sys

import torch


TRAINING_ROOT = Path(__file__).resolve().parents[1]
sys.path.append(str(TRAINING_ROOT))

from configs import load_config
from models.model_service import ModelService


def export(config):
    service = ModelService(config)
    model = service.model
    model.eval()
    encoder = model.encoder
    decoder = model.decoder

    dummy_source = torch.randint(
        0,
        service.source_vocab.size(),
        (1, config["max_source_length"]),
        dtype=torch.long,
    )

    dummy_target = torch.randint(
        0,
        service.target_vocab.size(),
        (1, config["max_target_length"]),
        dtype=torch.long,
    )

    export_dir = config["export_dir"]
    export_dir.mkdir(
        parents=True,
        exist_ok=True,
    )

    output_path = export_dir / "model.onnx"
    encoder_path = export_dir / "encoder.onnx"
    decoder_path = export_dir / "decoder.onnx"

    torch.onnx.export(
        model,
        (dummy_source, dummy_target),
        output_path,
        export_params=True,
        external_data=False,
    )

    torch.onnx.export(
        encoder,
        dummy_source,
        encoder_path,
        input_names=["source"],
        output_names=["hidden", "cell"],
        opset_version=17,
    )

    dummy_token = torch.randint(
        0,
        service.target_vocab.size(),
        (1,),
        dtype=torch.long,
    )

    dummy_hidden = torch.randn(
        1,
        1,
        config["hidden_dim"],
    )

    dummy_cell = torch.randn(
        1,
        1,
        config["hidden_dim"],
    )

    torch.onnx.export(
        decoder,
        (
            dummy_token,
            dummy_hidden,
            dummy_cell,
        ),
        decoder_path,
        input_names=[
            "token",
            "hidden",
            "cell",
        ],
        output_names=[
            "prediction",
            "hidden_out",
            "cell_out",
        ],
        opset_version=17,
    )

    print("ONNX export complete")
    print(output_path)
    print(encoder_path)
    print(decoder_path)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("model")
    args = parser.parse_args()

    export(load_config(args.model))


if __name__ == "__main__":
    main()
