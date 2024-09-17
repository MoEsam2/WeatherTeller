package com.example.weatherteller.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherteller.data.source.remote.model.CurrentWeather
import com.example.weatherteller.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(application)
        ).get(WeatherViewModel::class)

        viewModel.getCurrentWeather()
        viewModel.weatherData.observe(this) { currentWeather ->
            currentWeather?.let {
                val iconId = currentWeather.weather[0].icon
                val imgUrl = "https://openweathermap.org/img/wn/$iconId@4x.png"
                Glide.with(binding.imageWeather.context).load(imgUrl).into(binding.imageWeather)
                addCallBack(currentWeather)
            }

        }
        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

    }

    private fun addCallBack(currentWeather: CurrentWeather) {
        binding.textViewSunSet.text = SimpleDateFormat(
            "hh:mm a",
            Locale.ENGLISH
        ).format(currentWeather.sys.sunset * 1000)

        binding.textViewSunRise.text = SimpleDateFormat(
            "hh:mm a",
            Locale.ENGLISH
        ).format(currentWeather.sys.sunrise * 1000)

        binding.apply {
            textViewWeatherDisc.text = currentWeather.weather[0].description
            textViewWind.text = " ${currentWeather.wind.speed} KM/H"
            textViewLocation.text = "${currentWeather.name}\n${currentWeather.sys.country}"
            textViewTemp.text = "${currentWeather.main.temp.toInt()}°C"
            textViewWeatherFeelsLike.text =
                "Feels like: ${currentWeather.main.feels_like.toInt()}°C"
            textViewMinTemp.text = "Min: ${currentWeather.main.temp_min.toInt()}°C"
            textViewMaxTemp.text = "Max: ${currentWeather.main.temp_max.toInt()}°C"
            textViewHumidity.text = "${currentWeather.main.humidity} %"
            textViewPressure.text = "${currentWeather.main.pressure} hPa"
            textViewAirQuality.text = "Air quality \n Fair"
        }
    }


}