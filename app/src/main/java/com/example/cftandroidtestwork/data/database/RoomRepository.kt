package com.example.cftandroidtestwork.data.database

import android.content.Context
import androidx.room.Room
import com.example.cftandroidtestwork.data.database.entity.CurrencyRoomItem
import com.example.cftandroidtestwork.data.internet.CurrencyInternetRepository

private const val DATABASE_NAME = "DB.db"
class RoomRepository (context: Context){
    companion object{
        private var INSTANCE: RoomRepository? = null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = RoomRepository(context)
            }
        }
        fun get(): RoomRepository{
            return INSTANCE ?: throw IllegalStateException("RoomRepository = null")
        }
    }

    private val database: RoomDatabaseCurrency = Room.databaseBuilder(
        context.applicationContext,
        RoomDatabaseCurrency::class.java,
        DATABASE_NAME
    )
        .build()
    private val databaseDao = database.roomDao()
    fun getItem() = databaseDao.getItem()
    fun setItem(item: CurrencyRoomItem) = databaseDao.setData(item)
}

