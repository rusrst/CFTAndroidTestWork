package com.example.cftandroidtestwork.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cftandroidtestwork.data.database.entity.ConverterRoom
import com.example.cftandroidtestwork.data.database.entity.CurrencyRoomItem

@Database(entities = [CurrencyRoomItem::class],
    version = 1, exportSchema = true)
@TypeConverters(ConverterRoom::class)
abstract class RoomDatabaseCurrency: RoomDatabase() {
    abstract fun roomDao(): RoomInterface
}