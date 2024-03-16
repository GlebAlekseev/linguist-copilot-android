import nltk
from nltk.tokenize import word_tokenize
from nltk import pos_tag

nltk.download('punkt')
nltk.download('averaged_perceptron_tagger')

def process(text):
    tokens = word_tokenize(text)
    pos_tags = pos_tag(tokens)
    word_pos_pairs = [(word, pos) for word, pos in pos_tags]
    print(word_pos_pairs)
    return word_pos_pairs