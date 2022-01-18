package com.example.cftandroidtestwork.data.database.entity

import androidx.room.TypeConverter
import com.example.cftandroidtestwork.data.json.ValuteAndName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConverterRoom {
    @TypeConverter
    fun fromValutes(list: List<ValuteAndName>?): String?{
        if (list == null) return null
        return Json.encodeToString(list)
    }
    @TypeConverter
    fun toValutes (string: String?): List<ValuteAndName>?{
        if (string == null) return null
        return Json.decodeFromString(string)
    }
}