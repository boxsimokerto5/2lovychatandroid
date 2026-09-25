package com.example.data.supabase

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Tolerant adapters for Moshi to prevent deserialization crashes when Supabase returns:
 * - ISO-8601 strings or formatted timestamps for Long fields (like created_at timestamptz)
 * - Numeric IDs or values for String fields (like id: 123)
 * - Hex color strings (like "#FB8C00") for Long fields (author_avatar_hex)
 */
class FlexibleTypeAdapters {

    @FromJson
    fun longFromJson(reader: JsonReader): Long? {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>()
            return null
        }
        if (reader.peek() == JsonReader.Token.NUMBER) {
            return reader.nextLong()
        }
        if (reader.peek() == JsonReader.Token.BOOLEAN) {
            return if (reader.nextBoolean()) 1L else 0L
        }
        if (reader.peek() == JsonReader.Token.STRING) {
            val raw = reader.nextString().trim()
            if (raw.isEmpty()) return null
            
            raw.toLongOrNull()?.let { return it }
            raw.toDoubleOrNull()?.let { return it.toLong() }

            if (raw.startsWith("#")) {
                raw.removePrefix("#").toLongOrNull(16)?.let { return 0xFF000000L or it }
            }
            if (raw.startsWith("0x", ignoreCase = true)) {
                raw.substring(2).toLongOrNull(16)?.let { return it }
            }

            // ISO-8601 parsing (Supabase default timestamptz)
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val instant = java.time.Instant.parse(raw)
                    return instant.toEpochMilli()
                }
            } catch (_: Throwable) {}

            val patterns = arrayOf(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss"
            )
            for (pattern in patterns) {
                try {
                    val sdf = SimpleDateFormat(pattern, Locale.US)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    val parsed = sdf.parse(raw)
                    if (parsed != null) return parsed.time
                } catch (_: Throwable) {}
            }
            return null
        }
        reader.skipValue()
        return null
    }

    @ToJson
    fun longToJson(writer: JsonWriter, value: Long?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }

    @FromJson
    fun stringFromJson(reader: JsonReader): String? {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>()
            return null
        }
        if (reader.peek() == JsonReader.Token.NUMBER) {
            return reader.nextString()
        }
        if (reader.peek() == JsonReader.Token.BOOLEAN) {
            return reader.nextBoolean().toString()
        }
        return reader.nextString()
    }

    @ToJson
    fun stringToJson(writer: JsonWriter, value: String?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}
