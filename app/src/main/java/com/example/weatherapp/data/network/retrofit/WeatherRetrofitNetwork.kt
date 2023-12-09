package com.example.weatherapp.data.network.retrofit

import com.example.weatherapp.Params.API_KEY
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.network.model.ForecastResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

 interface WeatherApi {
    @GET("data/2.5/forecast")
    suspend fun geForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("appid") apiKey: String
    ): ForecastResponse

}

class WeatherRetrofitNetwork : WeatherNetworkDataSource {
    private val weatherApi: WeatherApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/")
            .build()
            .create(WeatherApi::class.java)

    override suspend fun getForecast(latitude:Double,longitude: Double,language:String): ForecastResponse {
        return weatherApi.geForecast(latitude =latitude,longitude=longitude,language= language,API_KEY)
    }
}