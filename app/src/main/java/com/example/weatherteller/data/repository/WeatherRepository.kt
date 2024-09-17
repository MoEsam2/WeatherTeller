package com.example.weatherteller.data.repository

import com.example.weatherteller.data.source.remote.model.CurrentWeather
import com.example.weatherteller.data.source.remote.network.RetrofitModule
import retrofit2.Response

class WeatherRepository {
    private val api = RetrofitModule.api

    suspend fun getCurrentWeather(
        city: String,
        units: String,
        apiKey: String,
    ): Response<CurrentWeather> {
        return api.getCurrentWeather(city,units,apiKey)
    }
}