package com.example.weatherapp.utils

import com.example.weatherapp.utils.Params.CELSIUS
import com.example.weatherapp.utils.Params.FAHRENHEIT
import com.example.weatherapp.utils.Params.KELVIN

class ConvertUnits {
    companion object{
      private  var approximateTemp= ApproximateTemp()

        fun convertTemp(temp:Double,tempUnit:String) :String{
            var result=""
            when(tempUnit){
                CELSIUS -> result= approximateTemp(temp - 273.15) + "\u00B0"
                KELVIN -> result= approximateTemp(273.5 + ((temp - 32.0) * (5.0/9.0))) + "\u00B0"
                FAHRENHEIT -> result= approximateTemp(temp) + "\u00B0"
                else -> result= approximateTemp(temp) + "\u00B0"
            }
            return result
        }
    }
}