package pro.linguistcopilot.feature.word.controller

import pro.linguistcopilot.feature.word.entity.WordInfo

interface WordInfoController {
    fun getWordInfo(word: String): WordInfo?
}