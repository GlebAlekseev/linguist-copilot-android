package pro.linguistcopilot.feature.transcription.controller

import net.sf.extjwnl.data.IndexWordSet
import net.sf.extjwnl.data.PointerType
import net.sf.extjwnl.data.Word
import net.sf.extjwnl.dictionary.Dictionary
import pro.linguistcopilot.feature.word.controller.WordInfoController
import pro.linguistcopilot.feature.word.entity.WordInfo
import javax.inject.Inject

class WordInfoControllerImpl @Inject constructor() : WordInfoController {
    override fun getWordInfo(word: String): WordInfo? {
        val dictionary: Dictionary = Dictionary.getDefaultResourceInstance()
        val indexWordSets: IndexWordSet = dictionary.lookupAllIndexWords(word)
        var wordInfo: WordInfo? = null
        for (indexWord in indexWordSets.getIndexWordCollection()) {
            val synset = indexWord.senses
            val partOfSpeech = mutableListOf<String>()
            val synonyms = mutableListOf<String>()
            val antonyms = mutableListOf<String>()
            val hyponyms = mutableListOf<String>()
            val hypernyms = mutableListOf<String>()
            val derivationalForms = mutableListOf<String>()
            val inflections = mutableListOf<String>()
            val seeAlso = mutableListOf<String>()
            val verbGroups = mutableListOf<String>()
            val examples = mutableListOf<String>()
            for (sense in synset) {
                val words: MutableList<Word>? = sense.words
                if (words != null) {
                    for (w in words) {
                        val pos: String = indexWord.pos.name
                        partOfSpeech.add(pos)
                        synonyms.add(w.lemma)
                        val antonymPointers = w.getPointers(PointerType.ANTONYM)
                        for (antonymPointer in antonymPointers) {
                            val antonymSynset = antonymPointer.targetSynset
                            val antonymWords = antonymSynset.words
                            for (antonymWord in antonymWords) {
                                antonyms.add(antonymWord.lemma)
                            }
                        }
                        val hyponymPointers = sense.getPointers(PointerType.HYPONYM)
                        for (hyponymPointer in hyponymPointers) {
                            val hyponymSynset = hyponymPointer.targetSynset
                            val hyponymWords = hyponymSynset.words
                            for (hyponymWord in hyponymWords) {
                                hyponyms.add(hyponymWord.lemma)
                            }
                        }
                        val hypernymPointers = sense.getPointers(PointerType.HYPERNYM)
                        for (hypernymPointer in hypernymPointers) {
                            val hypernymSynset = hypernymPointer.targetSynset
                            val hypernymWords = hypernymSynset.words
                            for (hypernymWord in hypernymWords) {
                                hypernyms.add(hypernymWord.lemma)
                            }
                        }
                        val derivationalFormPointers = sense.getPointers(PointerType.DERIVATION)
                        for (derivationalFormPointer in derivationalFormPointers) {
                            val derivationalFormSynset = derivationalFormPointer.targetSynset
                            val derivationalFormWords = derivationalFormSynset.words
                            for (derivationalFormWord in derivationalFormWords) {
                                derivationalForms.add(derivationalFormWord.lemma)
                            }
                        }
                        val inflectionPointers = sense.getPointers(PointerType.SIMILAR_TO)
                        for (inflectionPointer in inflectionPointers) {
                            val inflectionSynset = inflectionPointer.targetSynset
                            val inflectionWords = inflectionSynset.words
                            for (inflectionWord in inflectionWords) {
                                inflections.add(inflectionWord.lemma)
                            }
                        }
                        val seeAlsoPointers = w.getPointers(PointerType.SEE_ALSO)
                        for (seeAlsoPointer in seeAlsoPointers) {
                            val seeAlsoSynset = seeAlsoPointer.targetSynset
                            val seeAlsoWords = seeAlsoSynset.words
                            for (seeAlsoWord in seeAlsoWords) {
                                seeAlso.add(seeAlsoWord.lemma)
                            }
                        }
                        val verbGroupPointers = w.getPointers(PointerType.VERB_GROUP)
                        for (verbGroupPointer in verbGroupPointers) {
                            val verbGroupSynset = verbGroupPointer.targetSynset
                            val verbGroupWords = verbGroupSynset.words
                            for (verbGroupWord in verbGroupWords) {
                                verbGroups.add(verbGroupWord.lemma)
                            }
                        }
                    }
                }
                val gloss = sense.gloss
                examples.add(gloss)
            }
            wordInfo = WordInfo(
                word,
                partOfSpeech.toSet().toList(),
                synonyms.toSet().toList(),
                antonyms.toSet().toList(),
                hyponyms.toSet().toList(),
                hypernyms.toSet().toList(),
                derivationalForms.toSet().toList(),
                inflections.toSet().toList(),
                seeAlso.toSet().toList(),
                verbGroups.toSet().toList(),
                examples
            )
        }
        dictionary.close()
        return wordInfo
    }
}