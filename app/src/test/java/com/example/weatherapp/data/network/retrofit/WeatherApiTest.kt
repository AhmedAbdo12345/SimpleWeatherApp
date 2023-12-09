package com.example.weatherapp.data.network.retrofit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.Params.API_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WeatherApiTest {
    var weatherApi: WeatherApi?=null

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val retrofit= Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        weatherApi= retrofit.create(WeatherApi::class.java)    }

    @After
    fun tearDown() {
        weatherApi = null
    }

    @Test
    fun getWeather() = runBlocking<Unit>{
        //Given
        val lat=27.55
        val lng=62.58
        val language="en"
        //   When
      val response =  weatherApi?.geForecast(lat,lng,language,API_KEY)

        // Then
           MatcherAssert.assertThat(response,  IsNull.notNullValue())

    }
}