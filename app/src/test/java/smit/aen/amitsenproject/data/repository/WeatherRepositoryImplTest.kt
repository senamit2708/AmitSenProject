package smit.aen.amitsenproject.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import smit.aen.amitsenproject.data.models.AirQuality
import smit.aen.amitsenproject.data.models.Condition
import smit.aen.amitsenproject.data.models.Current
import smit.aen.amitsenproject.data.models.Location
import smit.aen.amitsenproject.data.models.WeatherResponse
import smit.aen.amitsenproject.di.WeatherApiService
import smit.aen.amitsenproject.utils.Resource

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {
    private val apiService: WeatherApiService = mockk()
    private lateinit var repository: WeatherRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = WeatherRepositoryImpl(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWeather emits Loading and Success when API returns data`() = runTest {
        // Arrange
        val fakeWeather = WeatherResponse(
            location = Location("Delhi", "India"),
            current = Current(
                temp_c = 30f,
                condition = Condition("Sunny", "//icon.png"),
                air_quality = AirQuality(10f, 20f, 30f, 1, 2)
            )
        )

        coEvery { apiService.getCurrentWeather("Delhi") } returns Response.success(fakeWeather)

        // Act
        val emissions = repository.getWeather("Delhi").toList()

        // Assert
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Success)
        assertEquals(30f, (emissions[1] as Resource.Success).data.current.temp_c)
    }

    @Test
    fun `getWeather emits Loading and Error when API fails`() = runTest {
        // Arrange
        coEvery { apiService.getCurrentWeather("Delhi") } returns Response.error(
            404,
            ResponseBody.create(null, "Not Found")
        )

        // Act
        val emissions = repository.getWeather("Delhi").toList()

        // Assert
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
    }

    @Test
    fun `getWeather emits Loading and Error when Exception occurs`() = runTest {
        // Arrange
        coEvery { apiService.getCurrentWeather("Delhi") } throws RuntimeException("Network error")

        // Act
        val emissions = repository.getWeather("Delhi").toList()

        // Assert
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals("Network error", (emissions[1] as Resource.Error).message)
    }
}