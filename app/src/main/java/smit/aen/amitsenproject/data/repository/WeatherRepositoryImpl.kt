package smit.aen.amitsenproject.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import smit.aen.amitsenproject.data.models.WeatherResponse
import smit.aen.amitsenproject.di.WeatherApiService
import smit.aen.amitsenproject.domain.repository.WeatherRepository
import smit.aen.amitsenproject.utils.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeather(location: String): Flow<Resource<WeatherResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getCurrentWeather(location)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error", e))
        }
    }.flowOn(Dispatchers.IO)
}
