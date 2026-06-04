from pathlib import Path
import argparse
import sys

import pandas as pd
import torch


TRAINING_ROOT = Path(__file__).resolve().parents[1]
sys.path.append(str(TRAINING_ROOT))

from configs import load_config
from models.model_service import ModelService


def evaluate(config):
    service = ModelService(config)
    test_df = pd.read_csv(
        config["dataset_dir"] / "test.csv"
    )

    correct = 0
    total = 0
    wrong_examples = 0

    for _, row in test_df.iterrows():
        source_word = str(row[config["source_column"]]).lower()
        expected = str(row[config["target_column"]])
        predicted_word = service.transliterate(source_word)

        if predicted_word == expected:
            correct += 1
        elif wrong_examples < 30:
            print()
            print("Source   :", source_word)
            print("Expected :", expected)
            print("Predicted:", predicted_word)
            wrong_examples += 1

        total += 1

    accuracy = 0 if total == 0 else correct / total * 100

    print()
    print("Model:", config["name"])
    print(f"Correct: {correct}")
    print(f"Total: {total}")
    print(f"Word Accuracy: {accuracy:.2f}%")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("model")
    args = parser.parse_args()

    evaluate(load_config(args.model))


if __name__ == "__main__":
    main()
