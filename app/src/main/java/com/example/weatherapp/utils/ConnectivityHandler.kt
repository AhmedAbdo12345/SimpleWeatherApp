package com.example.weatherapp.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class ConnectivityHandler(private val connectivityManager: ConnectivityManager) {
    fun isConnected(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}