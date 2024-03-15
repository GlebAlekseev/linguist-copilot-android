package pro.linguistcopilot.feature.word.entity

data class WordInfo(
    val word: String,
    val partOfSpeech: List<String>,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val hyponyms: List<String>,
    val hypernyms: List<String>,
    val derivationalForms: List<String>,
    val inflections: List<String>,
    val seeAlso: List<String>,
    val verbGroups: List<String>,
    val examples: List<String>
)