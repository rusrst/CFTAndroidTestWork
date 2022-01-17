package com.example.cftandroidtestwork

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import androidx.lifecycle.MutableLiveData
import com.example.cftandroidtestwork.data.internet.CurrencyInternetRepository
import com.example.cftandroidtestwork.data.json.CurrentCurrency
import com.example.cftandroidtestwork.data.json.CurrentCurrencyWithListValuteAndName
import com.example.cftandroidtestwork.data.json.ValuteAndName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


private const val TAG = "BACKGROUND Thread"
private const val MESSAGE_DOWNLOAD = 0
class WorkerThread(private val liveData: MutableLiveData<CurrentCurrencyWithListValuteAndName?>): HandlerThread(TAG) {
    private val currencyInternetRepository = CurrencyInternetRepository.get()
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
                }
            }
        }
    }

    fun returnData( url: String){
        mHandler.obtainMessage(MESSAGE_DOWNLOAD,0, 0,  url)
            .sendToTarget()
    }

    fun handleRequest(url: String){
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
        liveData.postValue(result)
    }
}

