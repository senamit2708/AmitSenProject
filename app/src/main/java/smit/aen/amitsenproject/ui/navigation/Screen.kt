package smit.aen.amitsenproject.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AlertDetail : Screen("alert_detail/{alertId}") {
        fun createRoute(alertId: String) = "alert_detail/$alertId"
    }
}