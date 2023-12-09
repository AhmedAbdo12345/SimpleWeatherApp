package com.example.weatherapp.utils

import android.location.Address

interface CurrentLocationStatue {
    fun success(list :List<Address>)
}