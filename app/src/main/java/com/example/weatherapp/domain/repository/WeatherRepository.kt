package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.network.model.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
     fun getWeather(latitude:Double,longitude: Double,language:String): Flow<ForecastResponse>

}