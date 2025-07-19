package smit.aen.amitsenproject.data.models

data class Current(
    val temp_c: Float,
    val condition: Condition,
    val air_quality: AirQuality
)