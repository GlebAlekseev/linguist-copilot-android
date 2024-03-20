package pro.linguistcopilot.feature.textProcessing.controller

import android.content.Context
import opennlp.tools.langdetect.LanguageDetectorME
import opennlp.tools.langdetect.LanguageDetectorModel
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.tokenize.SimpleTokenizer
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.feature.textProcessing.entity.TaggedText
import pro.linguistcopilot.feature.word.entity.Language
import java.util.LinkedList
import java.util.SortedMap
import javax.inject.Inject


class OpenNLPTextProcessingControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TextProcessingController {
    private val posDetectModelIn by lazy {
        context.assets.open("opennlp-en-ud-ewt-pos-1.0-1.9.3.bin")
    }
    private val sentenceDetectModelIn by lazy {
        context.assets.open("opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin")
    }
    private val langDetectModelIn by lazy {
        context.assets.open("langdetect-183.bin")
    }
    private val posModel by lazy { POSModel(posDetectModelIn) }
    private val sentenceDetectModel by lazy { SentenceModel(sentenceDetectModelIn) }
    private val languageDetectorModel by lazy { LanguageDetectorModel(langDetectModelIn) }

    private val tagger by lazy { POSTaggerME(posModel) }
    private val sentenceDetector by lazy { SentenceDetectorME(sentenceDetectModel) }
    private val languageDetector by lazy { LanguageDetectorME(languageDetectorModel) }

    private val tokenizer by lazy { SimpleTokenizer.INSTANCE }

    override fun getTaggedText(text: String): TaggedText {
        val tokens = tokenizer.tokenize(text)
        val tagsMap = sortedMapOf<Int, LinkedList<TaggedText.TAG>>()

        handlePOS(tagsMap, tokens, text)
        handleSentences(tagsMap, text)
        val language = getTextLanguage(text)
        return TaggedText(
            text = text,
            tags = tagsMap,
            language = language
        )
    }

    override fun getTextLanguage(text: String): Language {
        val lang = languageDetector.predictLanguage(text)
        return when (lang.lang) {
            "eng" -> Language.English
            else -> Language.Other(lang.lang).also { println("===== Lang.UNKNOWN ${lang.lang}") }
        }
    }

    private fun handleSentences(tagsMap: SortedMap<Int, LinkedList<TaggedText.TAG>>, text: String) {
        val sentences = sentenceDetector.sentDetect(text)
        var index = 0
        for (sentence in sentences) {
            val sentenceStartIndex = text.indexOf(sentence, index)
            if (tagsMap.containsKey(sentenceStartIndex)) {
                tagsMap[sentenceStartIndex]?.add(TaggedText.TAG.START_SENTENCE)
            } else {
                tagsMap[sentenceStartIndex] = LinkedList(listOf(TaggedText.TAG.START_SENTENCE))
            }
            val sentenceEndIndex = sentenceStartIndex + sentence.length
            if (tagsMap.containsKey(sentenceEndIndex)) {
                tagsMap[sentenceEndIndex]?.add(TaggedText.TAG.START_SENTENCE)
            } else {
                tagsMap[sentenceEndIndex] = LinkedList(listOf(TaggedText.TAG.END_SENTENCE))
            }
            index = sentenceEndIndex
        }
    }

    private fun handlePOS(
        tagsMap: SortedMap<Int, LinkedList<TaggedText.TAG>>,
        tokens: Array<String>,
        text: String
    ) {
        val tags = tagger.tag(tokens)
        var index = 0
        for (i in tokens.indices) {
            val wordStartIndex = text.indexOf(tokens[i], index)
            val tag = when (tags[i]) {
                "DET" -> TaggedText.TAG.DET
                "ADJ" -> TaggedText.TAG.ADJ
                "NOUN" -> TaggedText.TAG.NOUN
                "PUNCT" -> TaggedText.TAG.PUNCT
                "ADP" -> TaggedText.TAG.ADP
                "VERB" -> TaggedText.TAG.VERB
                else -> TaggedText.TAG.UNKNOWN.also { println("===== TaggedText.TAG.UNKNOWN ${tags[i]}") }
            }
            if (tagsMap.containsKey(wordStartIndex)) {
                tagsMap[wordStartIndex]?.add(tag)
            } else {
                tagsMap[wordStartIndex] = LinkedList(listOf(tag))
            }
            index = wordStartIndex + tokens[i].length
        }
    }
}