import json

import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer, PorterStemmer
from collections import defaultdict

nltk.download('punkt')
nltk.download('stopwords')
nltk.download('wordnet')


def preprocess_text(text):
    # Токенизация текста
    tokens = word_tokenize(text.lower())

    # Удаление стоп-слов
    stop_words = set(stopwords.words('english'))
    tokens = [token for token in tokens if token not in stop_words]

    # Лемматизация и стемминг токенов
    lemmatizer = WordNetLemmatizer()
    stemmer = PorterStemmer()
    preprocessed_tokens = [lemmatizer.lemmatize(stemmer.stem(token)) for token in tokens]

    return preprocessed_tokens


def preprocess_categories(category_keywords):
    # Применение стемминга и лемматизации к ключевым словам категорий
    stemmer = PorterStemmer()
    lemmatizer = WordNetLemmatizer()
    preprocessed_keywords = {}
    for category, keywords in category_keywords.items():
        preprocessed_keywords[category] = set()
        for keyword in keywords:
            stemmed_word = stemmer.stem(keyword)
            lemmatized_word = lemmatizer.lemmatize(keyword)
            if stemmed_word != lemmatized_word:  # Если стемминг и лемматизация дали разные результаты, добавляем оба
                preprocessed_keywords[category].add(stemmed_word)
                preprocessed_keywords[category].add(lemmatized_word)
            else:
                preprocessed_keywords[category].add(stemmed_word)

    return preprocessed_keywords


def categorize_text(text, category_keywords):
    # Предварительная обработка текста
    preprocessed_text = preprocess_text(text)

    # Определение категорий на основе ключевых слов
    # Предварительная обработка ключевых слов категорий
    preprocessed_keywords = preprocess_categories(category_keywords)

    # Подсчет вхождений ключевых слов в текст для каждой категории
    category_counts = defaultdict(int)
    for token in preprocessed_text:
        for category, keywords in preprocessed_keywords.items():
            if token in keywords:
                category_counts[category] += 1

    # Сортировка категорий по количеству вхождений
    sorted_categories = sorted(category_counts.items(), key=lambda x: x[1], reverse=True)

    # Возвращение списка наиболее вероятных категорий
    top_categories = [category for category, count in sorted_categories if count > 0]
    return top_categories


def process(text, category_keywords_json):
    category_keywords = json.loads(category_keywords_json)
    return categorize_text(text, category_keywords)


if __name__ == "__main__":
    # Ключевые слова категорий после предварительной обработки
    category_keywords = {
        'technology': ['computer', 'internet', 'software', 'programming'],
        'sports': ['football', 'soccer', 'basketball', 'tennis'],
        'politics': ['government', 'president', 'election', 'policy']
    }

    # Пример использования
    text = "The new programming language is revolutionizing the technology industry."
    result = process(text, json.dumps(category_keywords))
    print(result)
