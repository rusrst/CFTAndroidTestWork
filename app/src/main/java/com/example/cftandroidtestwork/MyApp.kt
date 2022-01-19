package com.example.cftandroidtestwork

import android.app.Application
import com.example.cftandroidtestwork.data.database.RoomRepository
import com.example.cftandroidtestwork.data.internet.CurrencyInternetRepository

class MyApp: Application() {
    override fun onCreate() {
        CurrencyInternetRepository.initialize()
        RoomRepository.initialize(this)
        GlobalState.initialize()
        super.onCreate()
    }
}