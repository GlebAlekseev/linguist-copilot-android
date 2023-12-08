package pro.linguistcopilot.features.reader.domain

import com.google.gson.*
import com.google.gson.JsonParseException
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonWriter
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.ReviewRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule
import pro.linguistcopilot.features.reader.core.book.RuleBigDataHelp
import pro.linguistcopilot.features.reader.core.book.RuleDataInterface
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import kotlin.math.ceil

val INITIAL_GSON: Gson by lazy {
    GsonBuilder()
        .registerTypeAdapter(
            object : TypeToken<Map<String?, Any?>?>() {}.type,
            MapDeserializerDoubleAsIntFix()
        )
        .registerTypeAdapter(Int::class.java, IntJsonDeserializer())
        .registerTypeAdapter(String::class.java, StringJsonDeserializer())
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()
}

val GSON: Gson by lazy {
    INITIAL_GSON.newBuilder()
        .registerTypeAdapter(ExploreRule::class.java, ExploreRule.jsonDeserializer)
        .registerTypeAdapter(SearchRule::class.java, SearchRule.jsonDeserializer)
        .registerTypeAdapter(BookInfoRule::class.java, BookInfoRule.jsonDeserializer)
        .registerTypeAdapter(TocRule::class.java, TocRule.jsonDeserializer)
        .registerTypeAdapter(ContentRule::class.java, ContentRule.jsonDeserializer)
        .registerTypeAdapter(ReviewRule::class.java, ReviewRule.jsonDeserializer)
        .create()
}

inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type

inline fun <reified T> Gson.fromJsonObject(json: String?): Result<T> {
    return kotlin.runCatching {
        if (json == null) {
            throw JsonSyntaxException("解析字符串为空")
        }
        fromJson(json, genericType<T>()) as T
    }
}

inline fun <reified T> Gson.fromJsonArray(json: String?): Result<List<T>> {
    return kotlin.runCatching {
        if (json == null) {
            throw JsonSyntaxException("解析字符串为空")
        }
        fromJson(
            json,
            TypeToken.getParameterized(List::class.java, T::class.java).type
        ) as List<T>
    }
}

inline fun <reified T> Gson.fromJsonObject(inputStream: InputStream?): Result<T> {
    return kotlin.runCatching {
        if (inputStream == null) {
            throw JsonSyntaxException("解析流为空")
        }
        val reader = InputStreamReader(inputStream)
        fromJson(reader, genericType<T>()) as T
    }
}

inline fun <reified T> Gson.fromJsonArray(inputStream: InputStream?): Result<List<T>> {
    return kotlin.runCatching {
        if (inputStream == null) {
            throw JsonSyntaxException("解析流为空")
        }
        val reader = InputStreamReader(inputStream)
        fromJson(
            reader,
            TypeToken.getParameterized(List::class.java, T::class.java).type
        ) as List<T>
    }
}

fun Gson.writeToOutputStream(out: OutputStream, any: Any) {
    val writer = JsonWriter(OutputStreamWriter(out, "UTF-8"))
    writer.setIndent("  ")
    if (any is List<*>) {
        writer.beginArray()
        any.forEach {
            it?.let {
                toJson(it, it::class.java, writer)
            }
        }
        writer.endArray()
    } else {
        toJson(any, any::class.java, writer)
    }
    writer.close()
}

/**
 *
 */
class StringJsonDeserializer : JsonDeserializer<String?> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): String? {
        return when {
            json.isJsonPrimitive -> json.asString
            json.isJsonNull -> null
            else -> json.toString()
        }
    }

}

/**
 * int类型转化失败时跳过
 */
class IntJsonDeserializer : JsonDeserializer<Int?> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int? {
        return when {
            json.isJsonPrimitive -> {
                val prim = json.asJsonPrimitive
                if (prim.isNumber) {
                    prim.asNumber.toInt()
                } else {
                    null
                }
            }

            else -> null
        }
    }

}

/**
 * 修复Int变为Double的问题
 */
class MapDeserializerDoubleAsIntFix :
    JsonDeserializer<Map<String, Any?>?> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): Map<String, Any?>? {
        @Suppress("unchecked_cast")
        return read(jsonElement) as? Map<String, Any?>
    }

    fun read(json: JsonElement): Any? {
        when {
            json.isJsonArray -> {
                val list: MutableList<Any?> = ArrayList()
                val arr = json.asJsonArray
                for (anArr in arr) {
                    list.add(read(anArr))
                }
                return list
            }

            json.isJsonObject -> {
                val map: MutableMap<String, Any?> =
                    LinkedTreeMap()
                val obj = json.asJsonObject
                val entitySet =
                    obj.entrySet()
                for ((key, value) in entitySet) {
                    map[key] = read(value)
                }
                return map
            }

            json.isJsonPrimitive -> {
                val prim = json.asJsonPrimitive
                when {
                    prim.isBoolean -> {
                        return prim.asBoolean
                    }

                    prim.isString -> {
                        return prim.asString
                    }

                    prim.isNumber -> {
                        val num: Number = prim.asNumber
                        // here you can handle double int/long values
                        // and return any type you want
                        // this solution will transform 3.0 float to long values
                        return if (ceil(num.toDouble()) == num.toLong().toDouble()) {
                            num.toLong()
                        } else {
                            num.toDouble()
                        }
                    }
                }
            }
        }
        return null
    }

}

interface BaseBook : RuleDataInterface {
    var name: String
    var author: String
    var bookUrl: String
    var kind: String?
    var wordCount: String?
    var variable: String?

    var infoHtml: String?
    var tocHtml: String?

    override fun putVariable(key: String, value: String?): Boolean {
        if (super.putVariable(key, value)) {
            variable = GSON.toJson(variableMap)
        }
        return true
    }

    fun putCustomVariable(value: String?) {
        putVariable("custom", value)
    }

    fun getCustomVariable(): String {
        return getVariable("custom")
    }

    override fun putBigVariable(key: String, value: String?) {
        RuleBigDataHelp.putBookVariable(bookUrl, key, value)
    }

    override fun getBigVariable(key: String): String? {
        return RuleBigDataHelp.getBookVariable(bookUrl, key)
    }

    fun getKindList(): List<String> {
        val kindList = arrayListOf<String>()
        wordCount?.let {
            if (it.isNotBlank()) kindList.add(it)
        }
        kind?.let {
            val kinds = it.splitNotBlank(",", "\n")
            kindList.addAll(kinds)
        }
        return kindList
    }
}

fun String.splitNotBlank(vararg delimiter: String, limit: Int = 0): Array<String> = run {
    this.split(*delimiter, limit = limit).map { it.trim() }.filterNot { it.isBlank() }
        .toTypedArray()
}