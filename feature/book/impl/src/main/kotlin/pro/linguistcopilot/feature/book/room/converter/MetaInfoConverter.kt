package pro.linguistcopilot.feature.book.room.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pro.linguistcopilot.feature.book.entity.MetaInfo


class MetaInfoConverter {
    @TypeConverter
    fun toMetaInfo(value: String): MetaInfo? {
        if (value == "") return null
        return Json.decodeFromString<MetaInfo>(value)
    }

    @TypeConverter
    fun fromMetaInfo(metaInfo: MetaInfo?): String {
        if (metaInfo == null) return ""
        return Json.encodeToString(metaInfo)
    }
}