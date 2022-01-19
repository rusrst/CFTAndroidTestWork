package com.example.cftandroidtestwork
import androidx.lifecycle.MutableLiveData


class GlobalState {
    val data:MutableLiveData<Boolean> = MutableLiveData(false)

    companion object{
        private var INSTANCE: GlobalState? = null
        fun initialize(){
            if (INSTANCE == null){
                INSTANCE = GlobalState()
            }
        }
        fun get(): GlobalState {
            return INSTANCE ?: throw IllegalStateException("GlobalState = null")
        }
    }
}