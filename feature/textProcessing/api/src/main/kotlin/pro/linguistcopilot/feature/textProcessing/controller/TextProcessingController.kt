package pro.linguistcopilot.feature.textProcessing.controller

import pro.linguistcopilot.feature.textProcessing.entity.Lang
import pro.linguistcopilot.feature.textProcessing.entity.TaggedText

interface TextProcessingController {
    fun getTaggedText(text: String): TaggedText
    fun getTextLanguage(text: String): Lang
}