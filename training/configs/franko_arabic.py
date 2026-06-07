from pathlib import Path


TRAINING_ROOT = Path(__file__).resolve().parents[1]

CONFIG = {
    "name": "franko-arabic",
    "source_language": "franko",
    "target_language": "arabic",
    "source_column": "Arabize",
    "target_column": "Arabic",
    "architecture": "seq2seq",
    "dataset_dir": TRAINING_ROOT / "datasets" / "franko-arabic",
    "checkpoint_dir": TRAINING_ROOT / "checkpoints" / "franko-arabic",
    "export_dir": TRAINING_ROOT / "exports" / "franko-arabic",
    "embedding_dim": 128,
    "hidden_dim": 256,
    "batch_size": 64,
    "epochs": 10,
    "learning_rate": 0.001,
    "max_source_length": 24,
    "max_target_length": 24,
}
