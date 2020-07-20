package com.example.datacovid19

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET("summary")
    fun getAllCountry(): Call<AllCountry>
}

interface InfoService{
    @GET
    fun getInfoService(@Url url: String?) : Call<List<InfoNegara>>
}