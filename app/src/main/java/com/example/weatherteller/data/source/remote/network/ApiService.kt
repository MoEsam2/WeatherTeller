package com.example.weatherteller.data.source.remote.network

import com.example.weatherteller.data.source.remote.Forecast.Forecast
import com.example.weatherteller.data.source.remote.model.CurrentWeather
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Response<CurrentWeather>

    @GET("forecast?")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ):Response<Forecast>
}