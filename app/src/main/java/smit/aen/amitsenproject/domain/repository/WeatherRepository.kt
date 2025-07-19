package smit.aen.amitsenproject.domain.repository

import kotlinx.coroutines.flow.Flow
import smit.aen.amitsenproject.data.models.WeatherResponse
import smit.aen.amitsenproject.utils.Resource

interface WeatherRepository {
    suspend fun getWeather(location: String): Flow<Resource<WeatherResponse>>
}