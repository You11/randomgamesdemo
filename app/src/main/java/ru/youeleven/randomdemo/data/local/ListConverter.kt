package ru.youeleven.randomdemo.data.local

import androidx.room.TypeConverter

class ListConverter {

    private val delimiter = "!@#$*()%^&"

    @TypeConverter
    fun fromList(value: List<String>?): String? {
        if (value == null) return null
        var result = ""
        value.forEach {
            result += "${it}${delimiter}"
        }
        return result.dropLast(delimiter.length)
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        if (value == null) return null
        return value.split(delimiter)
    }
}