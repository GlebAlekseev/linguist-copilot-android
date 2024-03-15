package pro.linguistcopilot.feature.word

object ArpaToIpa {
    private val monopthongs = mapOf(
        "AO" to "ɔ",
        "AO0" to "ɔ",
        "AO1" to "ɔ",
        "AO2" to "ɔ",
        "AA" to "ɑ",
        "AA0" to "ɑ",
        "AA1" to "ɑ",
        "AA2" to "ɑ",
        "IY" to "i",
        "IY0" to "i",
        "IY1" to "i",
        "IY2" to "i",
        "UW" to "u",
        "UW0" to "u",
        "UW1" to "u",
        "UW2" to "u",
        "EH" to "e",
        "EH0" to "e",
        "EH1" to "e",
        "EH2" to "e",
        "IH" to "ɪ",
        "IH0" to "ɪ",
        "IH1" to "ɪ",
        "IH2" to "ɪ",
        "UH" to "ʊ",
        "UH0" to "ʊ",
        "UH1" to "ʊ",
        "UH2" to "ʊ",
        "AH" to "ʌ",
        "AH0" to "ə",
        "AH1" to "ʌ",
        "AH2" to "ʌ",
        "AE" to "æ",
        "AE0" to "æ",
        "AE1" to "æ",
        "AE2" to "æ",
        "AX" to "ə",
        "AX0" to "ə",
        "AX1" to "ə",
        "AX2" to "ə"
    )

    private val dipthongs = mapOf(
        "EY" to "eɪ",
        "EY0" to "eɪ",
        "EY1" to "eɪ",
        "EY2" to "eɪ",
        "AY" to "aɪ",
        "AY0" to "aɪ",
        "AY1" to "aɪ",
        "AY2" to "aɪ",
        "OW" to "oʊ",
        "OW0" to "oʊ",
        "OW1" to "oʊ",
        "OW2" to "oʊ",
        "AW" to "aʊ",
        "AW0" to "aʊ",
        "AW1" to "aʊ",
        "AW2" to "aʊ",
        "OY" to "ɔɪ",
        "OY0" to "ɔɪ",
        "OY1" to "ɔɪ",
        "OY2" to "ɔɪ"
    )

    private val rColoredVowels = mapOf(
        "ER" to "ɜr",
        "ER0" to "ɜr",
        "ER1" to "ɜr",
        "ER2" to "ɜr",
        "AXR" to "ər",
        "AXR0" to "ər",
        "AXR1" to "ər",
        "AXR2" to "ər"
    )

    private val stops = mapOf(
        "P" to "p",
        "B" to "b",
        "T" to "t",
        "D" to "d",
        "K" to "k",
        "G" to "ɡ"
    )

    private val affricates = mapOf(
        "CH" to "tʃ",
        "JH" to "dʒ"
    )

    private val fricatives = mapOf(
        "F" to "f",
        "V" to "v",
        "TH" to "θ",
        "DH" to "ð",
        "S" to "s",
        "Z" to "z",
        "SH" to "ʃ",
        "ZH" to "ʒ",
        "HH" to "h"
    )

    private val nasals = mapOf(
        "M" to "m",
        "EM" to "m̩",
        "N" to "n",
        "EN" to "n̩",
        "NG" to "ŋ",
        "ENG" to "ŋ̍"
    )

    private val liquids = mapOf(
        "L" to "l",
        "EL" to "ɫ̩",
        "R" to "r"
    )

    private val semivowels = mapOf(
        "W" to "w",
        "Y" to "j",
        "Q" to "ʔ"
    )

    private val arpaToIpaLookup =
        monopthongs + dipthongs + rColoredVowels + stops + affricates + fricatives + nasals + liquids + semivowels

    fun arpaToIpa(arpa: String): String {
        return arpa.split(" ").joinToString(" ") { arpaToIpaLookup[it] ?: "?" }
    }
}