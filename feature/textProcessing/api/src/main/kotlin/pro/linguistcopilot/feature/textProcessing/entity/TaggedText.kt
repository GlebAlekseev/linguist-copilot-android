package pro.linguistcopilot.feature.textProcessing.entity

import java.util.SortedMap

data class TaggedText(
    val text: String,
    val tags: SortedMap<Int, out List<TAG>>,
    val language: Lang
) {
    enum class TAG {
        START_SENTENCE,
        END_SENTENCE,
        DET,
        ADJ,
        NOUN,
        PUNCT,
        ADP,
        VERB,
        UNKNOWN
    }
}