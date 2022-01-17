package com.example.cftandroidtestwork.data.internet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CurrencyApi {
    @GET
    fun getData(
        @Url url: String,
    ): Call<String>
}