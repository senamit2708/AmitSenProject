package smit.aen.amitsenproject.utils

fun Int.toAqiLevel(): String {
    return when (this) {
        1 -> "Good"
        2 -> "Moderate"
        3 -> "Unhealthy for Sensitive Groups"
        4 -> "Unhealthy"
        5 -> "Very Unhealthy"
        6 -> "Hazardous"
        else -> "Unknown"
    }
}