from pathlib import Path
import argparse
import sys


TRAINING_ROOT = Path(__file__).resolve().parents[1]
sys.path.append(str(TRAINING_ROOT))

from configs import load_config
from models.model_service import ModelService


def run(config):
    service = ModelService(config)

    while True:
        text = input(f"\n{config['source_language']} > ")
        words = text.split()

        result = [
            service.transliterate(word)
            for word in words
        ]

        print(
            f"{config['target_language']} >",
            " ".join(result),
        )


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("model")
    args = parser.parse_args()

    run(load_config(args.model))


if __name__ == "__main__":
    main()
