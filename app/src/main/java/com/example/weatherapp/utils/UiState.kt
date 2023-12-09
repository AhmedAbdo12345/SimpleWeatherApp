package com.example.weatherapp.utils

sealed interface UiState<out T> {
    data class Success<T>(val data: T): UiState<T>
    data object Loading: UiState<Nothing>
    data class Failed(val message:String): UiState<Nothing>

}