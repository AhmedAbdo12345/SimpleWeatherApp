package com.example.weatherapp.presentation.home

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.network.model.ForecastResponse
import com.example.weatherapp.databinding.RvDayBinding
import com.example.weatherapp.utils.ConvertUnits
import com.example.weatherapp.utils.IconsApp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class HomeAdapter(var forecastResponse: ForecastResponse?, var tempUnit:String) :
    RecyclerView.Adapter<HomeAdapter.DailyViewHolder>() {

    lateinit var binding: RvDayBinding


    class DailyViewHolder(var binding: RvDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        val inflate =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvDayBinding.inflate(inflate, parent, false)
        return DailyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        forecastResponse?.list?.get(position)?.let {
            holder.binding.tvDay.text = getDay(it.dt!!.toLong())
            Log.d("zxcvb", "onBindViewHolder: "+it.dt.toLong())
            holder.binding.tvHour.text = getCurrentTime(it.dt.toLong(), forecastResponse!!.city?.timezone.toString())

            it.weather?.get(0)?.let { it1 ->
                IconsApp.getSuitableIcon(
                    it1.icon.toString(),
                    holder.binding.imgStatusDay
                )

                holder.binding.tvDescriptionDay.text = it1.description
            }


            //---------this method to convert number like 19.7875789621245  to 19.79 ---------------------------------

            holder.binding.tvTempDay.text = ConvertUnits.convertTemp(it.main?.temp as Double, tempUnit = tempUnit)



        }

    }

    override fun getItemCount(): Int {
        return forecastResponse?.list?.size ?: 0
    }

    fun getDay(dt: Long): String {
        val cityTxtFormat = SimpleDateFormat("E")
        val cityTxtData = Date(dt.toLong() * 1000)
        return cityTxtFormat.format(cityTxtData)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(dt: Long, timezone: String, format: String = "K:mm a"): String {

        return try {
            val zoneId = ZoneId.of("Africa/Cairo")
            val instant = Instant.ofEpochSecond(dt)
            val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
            instant.atZone(zoneId).format(formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            "--:--"
        }
    }

}
