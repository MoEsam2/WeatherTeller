package com.example.weatherteller.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherteller.data.source.remote.Forecast.ForecastData
import com.example.weatherteller.databinding.RvItemForecastBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter(private val forecastArrayList: ArrayList<ForecastData>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(private val binding: RvItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)

        fun bind(currentItem: ForecastData) {
            binding.apply {
                val iconId = currentItem.weather[0].icon
                val imgUrl = "https://openweathermap.org/img/wn/$iconId@4x.png"
                Glide.with(imgItem.context).load(imgUrl).into(imgItem)

                tvItemTemp.text = "${currentItem.main.temp.toInt()} °C"
                tvItemStatus.text ="${currentItem.weather[0].description}"
                tvItemTime.text = displayTime(currentItem.dt_txt)
            }

        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun displayTime(dtTxt: String): CharSequence? {
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
            val dateTime = LocalDateTime.parse(dtTxt,input)
            return output.format(dateTime)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            RvItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = forecastArrayList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentItem = forecastArrayList[position]
        holder.bind(currentItem)
    }
}