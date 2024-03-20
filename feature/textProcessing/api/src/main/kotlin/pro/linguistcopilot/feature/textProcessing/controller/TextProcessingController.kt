package pro.linguistcopilot.feature.textProcessing.controller

import pro.linguistcopilot.feature.textProcessing.entity.TaggedText
import pro.linguistcopilot.feature.word.entity.Language

interface TextProcessingController {
    fun getTaggedText(text: String): TaggedText
    fun getTextLanguage(text: String): Language
}