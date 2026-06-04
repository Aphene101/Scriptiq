from importlib import import_module


MODEL_CONFIGS = {
    "franko-arabic": "franko_arabic",
    "arabic-english": "arabic_english",
    "arabic-franko": "arabic_franko",
}


def load_config(model_name):
    module_name = MODEL_CONFIGS.get(model_name)

    if module_name is None:
        valid = ", ".join(sorted(MODEL_CONFIGS))
        raise ValueError(f"Unknown model '{model_name}'. Valid models: {valid}")

    return import_module(f"configs.{module_name}").CONFIG
