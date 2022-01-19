package com.example.cftandroidtestwork.views.currencylist


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cftandroidtestwork.WorkerThread
import com.example.cftandroidtestwork.data.json.CurrentCurrencyWithListValuteAndName
import com.example.cftandroidtestwork.data.json.Valute

class CurrencyViewModel: ViewModel() {
    val data = MutableLiveData<CurrentCurrencyWithListValuteAndName?>()
    var workerThread: WorkerThread? = null
    override fun onCleared() {
        super.onCleared()
        workerThread?.quit()
    }
    var currentVal: Valute? = null
    var position: Int = 0
    val listNamed= mutableListOf<String>()}