package com.example.weatherapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.utils.Params.NETWORK_ERROR_MESSAGE
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.data.network.model.ForecastResponse
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.ConnectivityHandler
import com.example.weatherapp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val connectivityHandler: ConnectivityHandler
) : ViewModel() {

    private var _weather = MutableStateFlow<UiState<ForecastResponse>>(UiState.Loading)
    val weather = _weather.asStateFlow()



    fun getWeather(latitude: Double, longitude: Double, language: String) {
        viewModelScope.launch {
            if (connectivityHandler.isConnected()) {
                weatherRepository.getWeather(latitude, longitude, language).onStart {
                    _weather.emit(UiState.Loading)

                }.catch {
                    _weather.emit(UiState.Failed(it.localizedMessage!!))

                }.onEach {
                    _weather.emit(UiState.Success(it))

                }.launchIn(viewModelScope)

            } else {
                _weather.emit(UiState.Failed(NETWORK_ERROR_MESSAGE))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApplication
                WeatherViewModel(app.appDependencies.weatherRepository,app.appDependencies.connectivityHandler)
            }
        }
    }
}
