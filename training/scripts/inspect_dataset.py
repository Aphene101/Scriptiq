from datasets import load_dataset

dataset = load_dataset("UBC-NLP/nilechat-arabizi-egy")

train = dataset["train"]

print(f"Rows: {len(train):,}")

lengths = [len(row["text"]) for row in train.select(range(10000))]

print(f"Average length: {sum(lengths)/len(lengths):.2f}")

print("\nExamples:\n")

for i in range(5):
    print("=" * 50)
    print(train[i]["text"][:500])