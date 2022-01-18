package com.example.cftandroidtestwork

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cftandroidtestwork.data.database.RoomRepository
import com.example.cftandroidtestwork.data.database.entity.CurrencyRoomItem
import com.example.cftandroidtestwork.data.internet.CurrencyInternetRepository
import com.example.cftandroidtestwork.data.json.CurrentCurrency
import com.example.cftandroidtestwork.data.json.CurrentCurrencyWithListValuteAndName
import com.example.cftandroidtestwork.data.json.ValuteAndName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


private const val TAG = "BACKGROUND Thread"
private const val MESSAGE_DOWNLOAD = 0
private const val MESSAGE_DOWNLOAD_FROM_INTERNET = 1
class WorkerThread(private val liveData: MutableLiveData<CurrentCurrencyWithListValuteAndName?>): HandlerThread(TAG) {
    private val currencyInternetRepository = CurrencyInternetRepository.get()
    private val roomRepository = RoomRepository.get()
    init {
        start()
        looper
    }
    private var hasQuit = false
    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }
    lateinit var mHandler: Handler
    override fun onLooperPrepared() {
        mHandler = object: Handler(Looper.myLooper()!!){
            override fun handleMessage(msg: Message) {
                when (msg.what){
                    MESSAGE_DOWNLOAD -> handleRequest(msg.obj as String)
                    MESSAGE_DOWNLOAD_FROM_INTERNET -> loadInternet(msg.obj as String)
                }
            }
        }
    }

    fun returnData( url: String, id: Int){
        when (id){
            0 -> mHandler.obtainMessage(MESSAGE_DOWNLOAD,0, 0,  url)
                .sendToTarget()
            1 -> mHandler.obtainMessage(MESSAGE_DOWNLOAD_FROM_INTERNET,0, 0,  url)
                .sendToTarget()
        }

    }

    fun handleRequest(url: String){
        val item = roomRepository.getItem()
        if (item == null || item.valutes == null) loadInternet(url)
        else{
            Log.d("TAG", "ROOM")
            val result = CurrentCurrencyWithListValuteAndName()
            result.apply {
                Date = item.Date
                PreviousDate = item.PreviousDate
                PreviousURL = item.PreviousURL
                Timestamp = item.Timestamp
                valutes = item.valutes
            }
            liveData.postValue(result)
        }
    }
    fun loadInternet(url: String){
        Log.d("TAG", "INTERNET")
        val data:String? = try {
            currencyInternetRepository.getRequestFromUrl(url)
        } catch (e: Exception){
            "null"
        }
        val temp: CurrentCurrency?= try{
            Json.decodeFromString<CurrentCurrency>(data ?: "")
        }
        catch (e:Exception){
            CurrentCurrency()
        }
        val result = CurrentCurrencyWithListValuteAndName()
        if (temp?.valutes != null){
            result.apply {
                Date = temp.Date
                PreviousDate = temp.PreviousDate
                PreviousURL = temp.PreviousURL
                Timestamp = temp.Timestamp
                valutes = mutableListOf()
                val strings = temp.valutes.keys
                strings.forEach {
                    (valutes as MutableList<ValuteAndName>).add(ValuteAndName(it, temp.valutes.get(it)!!))
                }
            }
        }
        val saveResult = CurrencyRoomItem()
        saveResult.apply {
            Date = result.Date
            PreviousDate = result.PreviousDate
            PreviousURL = result.PreviousURL
            Timestamp = result.Timestamp
            valutes = result.valutes
        }
        if (saveResult.valutes != null){
            roomRepository.setItem(saveResult)
        }
        liveData.postValue(result)
    }
}

