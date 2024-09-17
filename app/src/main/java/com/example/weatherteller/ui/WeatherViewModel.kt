package com.example.weatherteller.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherteller.R
import com.example.weatherteller.data.repository.WeatherRepository
import com.example.weatherteller.data.source.remote.model.CurrentWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel(private val context: Context) : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val _weatherData = MutableLiveData<CurrentWeather>()
    val weatherData: LiveData<CurrentWeather> get() = _weatherData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = weatherRepository.getCurrentWeather(
                    "new york",
                    "metric",
                    context.getString(R.string.api_key)
                )

                if (response.isSuccessful) {
                    _weatherData.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {

                _error.postValue("Network error: ${e.message}")
            } catch (e: HttpException) {

                _error.postValue("HTTP error: ${e.message}")
            }
        }
    }

}