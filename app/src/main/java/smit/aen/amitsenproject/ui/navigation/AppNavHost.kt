package smit.aen.amitsenproject.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import smit.aen.amitsenproject.ui.screens.AlertDetailScreen
import smit.aen.amitsenproject.ui.screens.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { /* TopAppBar if needed */ },
        bottomBar = { /* BottomNavigation if needed */ }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onAlertClick = { alertId ->
                        navController.navigate(Screen.AlertDetail.createRoute(alertId))
                    }
                )
            }
            composable(
                route = Screen.AlertDetail.route,
                arguments = listOf(navArgument("alertId") { type = NavType.StringType })
            ) { backStackEntry ->
                val alertId = backStackEntry.arguments?.getString("alertId")
                AlertDetailScreen(alertId = alertId)
            }
        }
    }
}