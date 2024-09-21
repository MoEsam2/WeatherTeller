package com.example.weatherteller.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherteller.R
import com.example.weatherteller.adapter.ForecastAdapter
import com.example.weatherteller.data.source.remote.Forecast.ForecastData
import com.example.weatherteller.data.source.remote.model.CurrentWeather
import com.example.weatherteller.databinding.ActivityMainBinding
import com.example.weatherteller.databinding.BottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var bottomSheetLayoutBinding: BottomSheetLayoutBinding

    private val dialog: BottomSheetDialog by lazy {
        BottomSheetDialog(this, R.style.BottomSheetTheme).apply {
            setContentView(bottomSheetLayoutBinding.root)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomSheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
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
        binding.textViewSunSet.text = dateFormatConverter(
            currentWeather.sys.sunset.toLong()
        )

        binding.textViewSunRise.text = dateFormatConverter(
            currentWeather
                .sys.sunrise.toLong()
        )

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

        binding.textViewFiveDays.setOnClickListener {
            openDialog()
        }
    }

    private fun dateFormatConverter(date: Long): String =
        SimpleDateFormat(
            "hh:mm a",
            Locale.ENGLISH
        ).format(Date(date * 1000))

    private fun openDialog() {
        getForecast()

        bottomSheetLayoutBinding.recyclerViewForecast.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 1, RecyclerView.HORIZONTAL, false)
        }

        dialog.show()
    }


    private fun getForecast() {
        viewModel.getForecast()
        viewModel.forecastData.observe(this) { currentForecast ->
            currentForecast?.let {
                val forecastArray: ArrayList<ForecastData> =
                    currentForecast.list as ArrayList<ForecastData>
                val adapter = ForecastAdapter(forecastArray)
                bottomSheetLayoutBinding.recyclerViewForecast.adapter = adapter
                bottomSheetLayoutBinding.textViewFiveDays.text =
                    "Five days forecast in ${currentForecast.city.name}"
            }

        }
    }


}