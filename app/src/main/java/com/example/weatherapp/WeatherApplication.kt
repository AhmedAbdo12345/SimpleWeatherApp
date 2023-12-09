package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.di.AppDependencies


class WeatherApplication() : Application(){

    val appDependencies by lazy {
        AppDependencies(this)
    }
}
