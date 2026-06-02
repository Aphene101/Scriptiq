import torch
from torch.utils.data import Dataset


class CharDataset(Dataset):

    def __init__(
            self,
            dataframe,
            source_vocab,
            target_vocab,
            max_source_length=24,
            max_target_length=24
    ):

        self.df = dataframe

        self.source_vocab = source_vocab
        self.target_vocab = target_vocab

        self.max_source_length = max_source_length
        self.max_target_length = max_target_length

    def pad(self, sequence, max_length):

        pad_idx = 0

        if len(sequence) > max_length:
            sequence = sequence[:max_length]

        return sequence + (
                [pad_idx] *
                (max_length - len(sequence))
        )

    def __len__(self):
        return len(self.df)

    def __getitem__(self, idx):

        row = self.df.iloc[idx]

        source = self.source_vocab.encode(
            str(row["Arabize"])
        )

        target = self.target_vocab.encode(
            str(row["Arabic"])
        )

        source = self.pad(
            source,
            self.max_source_length
        )

        target = self.pad(
            target,
            self.max_target_length
        )

        return (
            torch.tensor(source, dtype=torch.long),
            torch.tensor(target, dtype=torch.long)
        )