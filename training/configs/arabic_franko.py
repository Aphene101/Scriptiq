from pathlib import Path


TRAINING_ROOT = Path(__file__).resolve().parents[1]

CONFIG = {
    "name": "arabic-franko",
    "source_language": "arabic",
    "target_language": "franko",
    "source_column": "Arabic",
    "target_column": "Franko",
    "architecture": "seq2seq",
    "dataset_dir": TRAINING_ROOT / "datasets" / "arabic-franko",
    "checkpoint_dir": TRAINING_ROOT / "checkpoints" / "arabic-franko",
    "export_dir": TRAINING_ROOT / "exports" / "arabic-franko",
    "embedding_dim": 128,
    "hidden_dim": 256,
    "batch_size": 64,
    "epochs": 10,
    "learning_rate": 0.001,
    "max_source_length": 24,
    "max_target_length": 24,
}
