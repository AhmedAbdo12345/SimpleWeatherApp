package com.example.weatherapp.data.repository

import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.network.model.ForecastResponse
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultWeatherRepository(private val weatherNetworkDataSource: WeatherNetworkDataSource) : WeatherRepository {

    override fun getWeather(latitude: Double, longitude: Double, language: String): Flow<ForecastResponse> = flow {

        emit(weatherNetworkDataSource.getForecast(latitude,longitude,language))
    }


}