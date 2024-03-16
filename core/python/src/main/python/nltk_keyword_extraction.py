import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer
from sklearn.feature_extraction.text import TfidfVectorizer

nltk.download('punkt')
nltk.download('stopwords')
nltk.download('wordnet')

def preprocess_text(text):
    # Токенизация текста
    tokens = word_tokenize(text)

    # Удаление стоп-слов и лемматизация
    stop_words = set(stopwords.words('english'))
    filtered_tokens = [word.lower() for word in tokens if word.isalnum() and word.lower() not in stop_words]
    lemmatizer = WordNetLemmatizer()
    lemmatized_tokens = [lemmatizer.lemmatize(word) for word in filtered_tokens]

    # Объединение токенов обратно в текст
    processed_text = ' '.join(lemmatized_tokens)
    return processed_text

def extract_keywords(processed_text, number_words):
    # Создание TF-IDF векторизатора
    vectorizer = TfidfVectorizer()

    # Преобразование текста в TF-IDF матрицу
    tfidf_matrix = vectorizer.fit_transform([processed_text])

    # Получение списка ключевых слов и их TF-IDF значений
    feature_names = vectorizer.get_feature_names_out()
    tfidf_values = tfidf_matrix.toarray()[0]

    # Создание словаря ключевых слов и их TF-IDF значений
    keywords = dict(zip(feature_names, tfidf_values))

    # Вывод ключевых слов с наибольшим TF-IDF значением
    top_keywords = sorted(keywords.items(), key=lambda x: x[1], reverse=True)[:number_words]
    return top_keywords

def process(text, number_words):
    processed_text = preprocess_text(text)
    keywords = extract_keywords(processed_text, number_words)
    return keywords

if __name__ == "__main__":
    # Пример текста
    text = """
    Natural language processing (NLP) is a subfield of linguistics, computer science, and artificial intelligence concerned with the interactions between computers and human language, in particular how to program computers to process and analyze large amounts of natural language data. The result is a computer capable of "understanding" the contents of documents, including the contextual nuances of the language within them. The technology can then accurately extract information and insights contained in the documents as well as categorize and organize the documents themselves.
    """
    number_words = 5
    keywords = process(text, number_words)
    print("Top keywords:")
    for keyword, tfidf_value in keywords:
        print(keyword, "-", tfidf_value)
