Smart City Hub - Project Documentation

1. Project Overview
The Smart City Hub app aggregates real-time data from multiple sources, focusing on:
Weather updates (temperature and conditions).
Air Quality Index (AQI) data (PM2.5, PM10, O3, etc.).
City alerts (road closures, severe weather).
Quick stats (traffic, emergency shortcuts).
The app emphasizes clean architecture, testability, and modularity using modern Android development tools.

2. Tech Stack
Key Technologies:
Jetpack Compose – Modern declarative UI.
Kotlin Coroutines + StateFlow – Asynchronous and reactive state handling.
Hilt (Dagger) – Dependency injection for clean and modular code.
Retrofit + OkHttp – Network calls.
Gson – JSON serialization/deserialization.
JUnit + mockk – Unit testing.

3. Layered Architecture
The project follows a layered MVVM architecture with clear separation of concerns.
Layers:
UI Layer (ui/)
What it contains: Screens (HomeScreen, AlertDetailScreen), navigation (AppNavHost), and WeatherViewModel.
Responsibility: Render UI, observe StateFlow from the ViewModel.
Domain Layer (domain/)
What it contains: WeatherRepository interface.
Responsibility: Acts as a contract between the UI/ViewModel and the data sources.

Data Layer (data/)
What it contains: Data models (WeatherResponse, Location, Current, AirQuality), repository implementation (WeatherRepositoryImpl), and API service (WeatherApiService).
Responsibility: Fetch data from remote APIs.

DI Layer (di/)
What it contains: Hilt modules like NetworkModule and RepositoryModule.
Responsibility: Provide dependencies (Retrofit, API service, repositories).

Utils Layer (utils/)
What it contains: AppConstants (Base URL, API key), extension functions (toAqiLevel), and Resourceclass.
Responsibility: Provide helper functions and shared constants.


4. Data Flow
Step-by-step:
HomeScreen triggers WeatherViewModel.fetchWeather().
WeatherViewModel requests data from the WeatherRepository interface.
WeatherRepositoryImpl calls the API using WeatherApiService.
The response (wrapped in Resource) flows back to the ViewModel.
UI observes the weatherState and updates Compose components accordingly.


5. Key Components
Resource: Sealed class for Loading, Success, and Error states.
WeatherViewModel: Uses StateFlow to expose Resource<WeatherResponse>.
Repository Pattern: Interface (WeatherRepository) + implementation (WeatherRepositoryImpl).
NetworkModule: Provides Retrofit, OkHttp, and API dependencies.

6. Error Handling
All API calls emit a Resource.Error state when failures occur.
try-catch blocks handle network exceptions.
Error messages are logged for debugging.

7. Testing
Repository Tests: Mocked WeatherApiService to simulate API responses.
ViewModel Tests: Validate state transitions (Loading → Success and Loading → Error).
mockk + JUnit ensure isolated, reliable tests.

8. Challenges & Solutions
State Management: Solved using StateFlow to propagate UI state.
Error Wrapping: Introduced Resource class for clean error handling.
Job Cancellation: Used cancelAndJoin() in ViewModel to avoid overlapping API calls.
9. Future Improvements
Room Database: Cache weather and AQI data for offline access.
CI/CD: Automate builds/tests with GitHub Actions and Fastlane.
Push Notifications: For real-time city alerts.
UI Testing: Add Compose UI test cases.

10. High-Level Design Diagram
[ UI Layer (Compose Screens + ViewModel) ]
            ↓
[ Domain Layer (WeatherRepository Interface) ]
            ↓
[ Data Layer (WeatherRepositoryImpl + Retrofit API) ]
            ↓
        [ Weather API ]

