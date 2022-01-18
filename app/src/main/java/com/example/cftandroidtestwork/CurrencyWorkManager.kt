package com.example.cftandroidtestwork

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cftandroidtestwork.data.database.RoomRepository
import com.example.cftandroidtestwork.data.database.entity.CurrencyRoomItem
import com.example.cftandroidtestwork.data.internet.CurrencyInternetRepository
import com.example.cftandroidtestwork.data.json.CurrentCurrency
import com.example.cftandroidtestwork.data.json.CurrentCurrencyWithListValuteAndName
import com.example.cftandroidtestwork.data.json.ValuteAndName
import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception

class CurrencyWorkManager(private val context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
         var url = "https://www.cbr-xml-daily.ru/daily_json.js"
        CurrencyInternetRepository.initialize()
        RoomRepository.initialize(context)
        val currencyInternetRepository = CurrencyInternetRepository.get()
        val roomRepository = RoomRepository.get()
        var data = ""
        var temp = CurrentCurrency()
        try {
            data = currencyInternetRepository.getRequestFromUrl(url)
        }
        catch (e: Exception){
            return Result.retry()
        }
        try{
            temp = Json.decodeFromString(data)
        }
        catch (e:Exception){
            return Result.retry()
        }
        val result = CurrentCurrencyWithListValuteAndName()
        if (temp.valutes != null){
            result.apply {
                Date = temp.Date
                PreviousDate = temp.PreviousDate
                PreviousURL = temp.PreviousURL
                Timestamp = temp.Timestamp
                valutes = mutableListOf()
                val strings = temp.valutes?.keys
                strings?.forEach {
                    (valutes as MutableList<ValuteAndName>).add(ValuteAndName(it, temp.valutes!![it]!!))
                }
            }
        }
        else {
            return Result.retry()
        }
        val saveResult = CurrencyRoomItem()
        saveResult.apply {
            Date = java.sql.Date(System.currentTimeMillis()).toString()
            PreviousDate = result.PreviousDate
            PreviousURL = result.PreviousURL
            Timestamp = result.Timestamp
            valutes = result.valutes
        }
        if (saveResult.valutes != null){
            roomRepository.setItem(saveResult)
        }
        return Result.success()
    }
}