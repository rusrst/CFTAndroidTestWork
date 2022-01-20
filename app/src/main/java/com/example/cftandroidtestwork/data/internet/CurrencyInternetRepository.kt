package com.example.cftandroidtestwork.data.internet

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class CurrencyInternetRepository{
    private var currencyApi: CurrencyApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://google.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        currencyApi = retrofit.create(CurrencyApi::class.java)
    }
    @Synchronized fun getRequestFromUrl(url: String): String
    {
        val data: Call<String> = currencyApi.getData(url)
        val response = data.execute()
        return response.body().toString()
    }

    companion object{
        private var INSTANCE: CurrencyInternetRepository? = null
        fun initialize(){
            if (INSTANCE == null){
                INSTANCE = CurrencyInternetRepository()
            }
        }
        fun get(): CurrencyInternetRepository {
            return INSTANCE ?: throw IllegalStateException("CurrencyInternetRepository = null")
        }
    }
}