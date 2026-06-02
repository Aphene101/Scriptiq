import torch
from torch.utils.data import Dataset


class CharDataset(Dataset):

    def __init__(
            self,
            dataframe,
            source_vocab,
            target_vocab
    ):

        self.df = dataframe

        self.source_vocab = source_vocab
        self.target_vocab = target_vocab

    def __len__(self):
        return len(self.df)

    def __getitem__(self, idx):

        row = self.df.iloc[idx]

        source = self.source_vocab.encode(
            row["Arabize"]
        )

        target = self.target_vocab.encode(
            row["Arabic"]
        )

        return (
            torch.tensor(source),
            torch.tensor(target)
        )