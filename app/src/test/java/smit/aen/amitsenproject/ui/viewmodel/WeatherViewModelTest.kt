package smit.aen.amitsenproject.ui.viewmodel

import android.util.Log
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import smit.aen.amitsenproject.data.models.AirQuality
import smit.aen.amitsenproject.data.models.Condition
import smit.aen.amitsenproject.data.models.Current
import smit.aen.amitsenproject.data.models.Location
import smit.aen.amitsenproject.data.models.WeatherResponse
import smit.aen.amitsenproject.domain.repository.WeatherRepository
import smit.aen.amitsenproject.utils.Resource

class WeatherViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WeatherViewModel
    private val repository: WeatherRepository = mockk()

    private val fakeWeather = WeatherResponse(
        location = Location("Delhi", "India"),
        current = Current(
            temp_c = 28f,
            condition = Condition("Sunny", "//icon.png"),
            air_quality = AirQuality(5f, 10f, 20f, 1, 2)
        )
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
//        every { Log.w(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeather emits Loading and Success`() = runTest {
        // Arrange
        val fakeWeather = WeatherResponse(
            location = Location("Delhi", "India"),
            current = Current(
                temp_c = 28f,
                condition = Condition("Sunny", "//icon.png"),
                air_quality = AirQuality(5f, 10f, 20f, 1, 2)
            )
        )
        coEvery { repository.getWeather("Delhi") } returns flowOf(Resource.Loading, Resource.Success(fakeWeather))

        // Act
        viewModel.fetchWeather("Delhi")
        advanceUntilIdle() // Let coroutine complete

        // Assert
        val state = viewModel.weatherState.value
        assertTrue(state is Resource.Success)
        assertEquals("Delhi", (state as Resource.Success).data.location.name)
    }

    @Test
    fun `fetchWeather emits Error`() = runTest {
        // Arrange
        coEvery { repository.getWeather("Delhi") } returns flowOf(Resource.Loading, Resource.Error("Network error"))

        // Act
        viewModel.fetchWeather("Delhi")
        advanceUntilIdle() // Let coroutine complete

        // Assert
        val state = viewModel.weatherState.value
        assertTrue(state is Resource.Error)
        assertEquals("Network error", (state as Resource.Error).message)
    }
}