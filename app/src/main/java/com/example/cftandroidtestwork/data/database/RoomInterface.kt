package com.example.cftandroidtestwork.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cftandroidtestwork.data.database.entity.CurrencyRoomItem

@Dao
interface RoomInterface {
    @Query ("SELECT * FROM CurrencyRoomItem where id = 0")
    fun getItem(): CurrencyRoomItem?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setData(data:CurrencyRoomItem)
}