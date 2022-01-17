package com.example.cftandroidtestwork.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cftandroidtestwork.WorkerThread
import com.example.cftandroidtestwork.data.json.CurrentCurrency
import com.example.cftandroidtestwork.data.json.CurrentCurrencyWithListValuteAndName

class CurrencyViewModel: ViewModel() {
    val data = MutableLiveData<CurrentCurrencyWithListValuteAndName?>()
    var workerThread: WorkerThread? = null
    override fun onCleared() {
        super.onCleared()
        workerThread?.quit()
    }
}