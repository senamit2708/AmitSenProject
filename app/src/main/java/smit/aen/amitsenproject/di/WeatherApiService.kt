package smit.aen.amitsenproject.di

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import smit.aen.amitsenproject.data.models.WeatherResponse

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("aqi") aqi: String = "yes"
    ): Response<WeatherResponse>

}