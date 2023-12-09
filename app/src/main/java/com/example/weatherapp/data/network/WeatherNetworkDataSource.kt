package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.model.ForecastResponse

interface WeatherNetworkDataSource {
    suspend fun getForecast(latitude:Double,longitude: Double,language:String): ForecastResponse
}