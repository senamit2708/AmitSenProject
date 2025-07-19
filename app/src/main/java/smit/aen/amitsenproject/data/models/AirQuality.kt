package smit.aen.amitsenproject.data.models

import com.google.gson.annotations.SerializedName

data class AirQuality(
    val pm2_5: Float,
    val pm10: Float,
    val o3: Float,
    @SerializedName("us-epa-index") val usEpaIndex: Int,
    @SerializedName("gb-defra-index") val gbDefraIndex: Int
)