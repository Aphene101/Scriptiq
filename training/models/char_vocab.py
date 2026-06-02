from collections import Counter


class CharVocab:

    PAD = "<PAD>"
    SOS = "<SOS>"
    EOS = "<EOS>"
    UNK = "<UNK>"

    def __init__(self):

        self.char_to_idx = {
            self.PAD: 0,
            self.SOS: 1,
            self.EOS: 2,
            self.UNK: 3
        }

        self.idx_to_char = {
            0: self.PAD,
            1: self.SOS,
            2: self.EOS,
            3: self.UNK
        }

    def build(self, words):

        counter = Counter()

        for word in words:
            counter.update(str(word))

        for char in sorted(counter.keys()):

            if char not in self.char_to_idx:

                idx = len(self.char_to_idx)

                self.char_to_idx[char] = idx
                self.idx_to_char[idx] = char

    def encode(self, text):

        result = [self.char_to_idx[self.SOS]]

        for char in text:
            result.append(
                self.char_to_idx.get(
                    char,
                    self.char_to_idx[self.UNK]
                )
            )

        result.append(
            self.char_to_idx[self.EOS]
        )

        return result

    def decode(self, indices):

        chars = []

        for idx in indices:

            char = self.idx_to_char.get(idx)

            if char in (
                    self.PAD,
                    self.SOS,
                    self.EOS
            ):
                continue

            chars.append(char)

        return "".join(chars)

    def size(self):
        return len(self.char_to_idx)