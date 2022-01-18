package com.example.cftandroidtestwork.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cftandroidtestwork.data.json.ValuteAndName
@Entity
data class CurrencyRoomItem(@PrimaryKey val id: Int = 0,
                            var Date: String? = null,
                            var PreviousDate: String? = null,
                            var PreviousURL: String? = null,
                            var Timestamp: String? = null,
                            @TypeConverters(ConverterRoom::class)
                            var valutes: List<ValuteAndName>? = null)
