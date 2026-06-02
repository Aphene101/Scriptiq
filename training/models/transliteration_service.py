from models.model_service import ModelService


class TransliterationService:

    def __init__(
            self,
            dictionary,
            model_service
    ):

        self.dictionary = dictionary
        self.model_service = model_service

    def transliterate_word(
            self,
            word
    ):

        result = self.dictionary.get(
            word.lower()
        )

        if result:
            return result

        return self.model_service.transliterate(
            word
        )

    def transliterate_text(
            self,
            text
    ):

        words = text.split()

        result = []

        for word in words:

            result.append(
                self.transliterate_word(
                    word
                )
            )

        return " ".join(result)