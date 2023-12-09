package com.example.weatherapp.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.network.retrofit.WeatherRetrofitNetwork
import com.example.weatherapp.data.repository.DefaultWeatherRepository
import com.example.weatherapp.utils.ConnectivityHandler

class AppDependencies(private val context: Context) {
    val connectivityHandler by lazy {
        ConnectivityHandler(context.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }
    val weatherRepository by lazy {
        val weatherNetworkDataSource: WeatherNetworkDataSource = WeatherRetrofitNetwork()
       DefaultWeatherRepository(weatherNetworkDataSource)
    }
}