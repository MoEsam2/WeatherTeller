package com.example.weatherteller.data.source.remote.Forecast

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)