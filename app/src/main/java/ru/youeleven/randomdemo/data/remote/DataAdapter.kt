package ru.youeleven.randomdemo.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateAdapter {

    @ToJson
    fun toJson(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
    }

    @FromJson
    fun fromJson(string: String): Date {
        val formats = arrayListOf("yyyy-MM-dd")

        formats.forEach {
            try {
                return SimpleDateFormat(it, Locale.US).parse(string) ?: Date()
            } catch (e: ParseException) {

            }
        }

        return Date()
    }
}