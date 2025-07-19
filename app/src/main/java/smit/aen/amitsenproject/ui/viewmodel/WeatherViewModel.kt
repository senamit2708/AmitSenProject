package smit.aen.amitsenproject.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import smit.aen.amitsenproject.data.models.WeatherResponse
import smit.aen.amitsenproject.domain.repository.WeatherRepository
import smit.aen.amitsenproject.utils.Resource
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private var fetchWeatherJob: Job? = null

    private val _weatherState = MutableStateFlow<Resource<WeatherResponse>>(Resource.Loading)
    val weatherState: StateFlow<Resource<WeatherResponse>> = _weatherState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchWeather(location: String) {
        viewModelScope.launch {
            fetchWeatherJob?.cancelAndJoin() // Cancel old job before starting a new one
            fetchWeatherJob = launch {
                _isLoading.value = true
                try {
                    repository.getWeather(location).collect { resource ->
                        _weatherState.value = resource
                    }
                } catch (e: CancellationException) {
                    Log.d("WeatherViewModel", "fetchWeather cancelled")
                } catch (e: Exception) {
                    _weatherState.value = Resource.Error(e.localizedMessage ?: "Unexpected error")
                } finally {
                    _isLoading.value = false
                    Log.d("WeatherViewModel", "fetchWeather finished")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchWeatherJob?.cancel()
    }
}