package smit.aen.amitsenproject.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import smit.aen.amitsenproject.R
import smit.aen.amitsenproject.data.models.WeatherResponse
import smit.aen.amitsenproject.ui.viewmodel.WeatherViewModel
import smit.aen.amitsenproject.utils.Resource
import smit.aen.amitsenproject.utils.toAqiLevel

@Composable
fun HomeScreen(
    onAlertClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState by viewModel.weatherState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchWeather("Delhi")
    }
    Scaffold(topBar = { HomeTopBar() }) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { WeatherStateHandler(weatherState) }
            item { Spacer(Modifier.height(24.dp)) }
            item { ShortcutGridSection() }
            item { Spacer(Modifier.height(24.dp)) }
            item { AlertsSection(onAlertClick = onAlertClick) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(title = {
        Text(
            text = "Smart City Hub",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }, actions = {
        IconButton(onClick = { /* TODO: Help/FAQ */ }) {
            Icon(Icons.Default.AddCircle, contentDescription = "Help")
        }
        IconButton(onClick = { /* TODO: Settings */ }) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
    )
}


@Composable
fun WeatherStateHandler(
    weatherState: Resource<WeatherResponse>, modifier: Modifier = Modifier
) {
    when (weatherState) {
        is Resource.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            QuickStatsSection(weatherState.data)
        }

        is Resource.Error -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${weatherState.message}", color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun QuickStatsSection(weather: WeatherResponse?) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Quick Stats",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WeatherStatCard(
                temp = weather?.current?.temp_c?.toInt()?.toString() ?: "--",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
            AQIStatCard(
                aqiLevel = weather?.current?.air_quality?.usEpaIndex?.toAqiLevel() ?: "Loading",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
            TrafficStatCard(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
        }
    }
}

@Composable
fun WeatherStatCard(
    temp: String, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), // Fill the card space to enable vertical centering
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sunny),
                    contentDescription = "Weather"
                )
                Spacer(Modifier.width(4.dp))
                Text("$temp°", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(6.dp))
            Text("Sunny", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun AQIStatCard(
    aqiLevel: String, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), // Fill the Card's space
            verticalArrangement = Arrangement.SpaceEvenly, // Evenly spaced vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
        ) {
            Text("AQI", style = MaterialTheme.typography.titleLarge)
            Text(aqiLevel, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun TrafficStatCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), // Makes verticalArrangement effective
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_car), contentDescription = "Weather"
            )
            Text("Traffic", style = MaterialTheme.typography.titleLarge)
        }
    }
}


// ShortcutGridSection.kt
@Composable
fun ShortcutGridSection() {
    val shortcuts = listOf(
        ShortcutUi("Public Transport", painterResource(id = R.drawable.ic_public_transport)),
        ShortcutUi("Emergency Services", painterResource(id = R.drawable.ic_emergency)),
        ShortcutUi("Waste Mgmt.", painterResource(id = R.drawable.ic_waste)),
        ShortcutUi("City Events", painterResource(id = R.drawable.ic_calendar))
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false
    ) {
        items(shortcuts) { shortcut ->
            ShortcutButton(shortcut)
        }
    }
}

data class ShortcutUi(val label: String, val icon: Painter)

@Composable
fun ShortcutButton(shortcut: ShortcutUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { /* TODO: Shortcut click */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(shortcut.icon, contentDescription = shortcut.label)
            Spacer(Modifier.width(12.dp))
            Text(shortcut.label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// AlertsSection.kt
@Composable
fun AlertsSection(onAlertClick: (String) -> Unit) {
    val alerts = listOf(
        AlertUi("1", "Road Closure", "Main St. closed this week"),
        AlertUi("2", "Severe Weather", "Thunderstorms expected"),
        AlertUi("3", "Power Outage", "Downtown, check updates")
    )
    Column {
        Text("City Alerts", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        alerts.forEach { alert ->
            AlertItem(alert, onClick = { onAlertClick(alert.id) })
            Spacer(Modifier.height(8.dp))
        }
    }
}

data class AlertUi(val id: String, val title: String, val description: String)

@Composable
fun AlertItem(alert: AlertUi, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(alert.title, style = MaterialTheme.typography.bodyLarge)
            Text(alert.description, style = MaterialTheme.typography.bodySmall)
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Go to alert",
            modifier = Modifier.size(24.dp)
        )
    }
}
