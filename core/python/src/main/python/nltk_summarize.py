import heapq

import nltk
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize, sent_tokenize

nltk.download('stopwords')
def preprocess_text(text):
    text = text.lower()
    sentences = sent_tokenize(text)
    stop_words = set(stopwords.words("english"))
    word_freq = {}
    for sentence in sentences:
        word_tokens = word_tokenize(sentence)
        for word in word_tokens:
            if word not in stop_words:
                stem_word = PorterStemmer().stem(word)
                if stem_word in word_freq:
                    word_freq[stem_word] += 1
                else:
                    word_freq[stem_word] = 1
    return word_freq, sentences


def summarize(text, num_sentences=3):
    word_freq, sentences = preprocess_text(text)
    max_freq = max(word_freq.values())
    for word in word_freq.keys():
        word_freq[word] = word_freq[word] / max_freq
    sentence_scores = {}
    for sentence in sentences:
        for word in word_tokenize(sentence.lower()):
            word = PorterStemmer().stem(word)
            if word in word_freq.keys():
                if len(sentence.split(' ')) < 30:
                    if sentence not in sentence_scores.keys():
                        sentence_scores[sentence] = word_freq[word]
                    else:
                        sentence_scores[sentence] += word_freq[word]
    summary_sentences = heapq.nlargest(num_sentences, sentence_scores, key=sentence_scores.get)
    summary = ' '.join(summary_sentences)
    return summary


def process(text, num_sentences):
    return summarize(text, num_sentences)
