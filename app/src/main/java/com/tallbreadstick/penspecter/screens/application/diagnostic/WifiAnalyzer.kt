package com.tallbreadstick.penspecter.screens.application.diagnostic

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.screens.application.penetration.SectionLabel
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import kotlinx.coroutines.delay
import kotlin.random.Random

@Preview
@Composable
fun WifiAnalyzer(navController: NavController? = null) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var throughput by remember { mutableStateOf(List(30) { Random.nextInt(10, 100) }) }
    var latency by remember { mutableStateOf(List(30) { Random.nextInt(20, 150) }) }

    // Simulate live updates every 1s
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            throughput = throughput.drop(1) + Random.nextInt(10, 100)
            latency = latency.drop(1) + Random.nextInt(20, 150)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navController?.let { Navbar(it) }

            Text(
                text = "WiFi Analyzer",
                fontSize = 28.sp,
                color = Color.White,
                fontFamily = DidactGothic,
                modifier = Modifier.padding(16.dp)
            )

            SectionLabel("Throughput (Mbps)")
            Chart(
                chart = lineChart(),
                model = entryModelOf(*throughput.mapIndexed { i, v -> i to v }.toTypedArray()),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp)
            )

            SectionLabel("Latency (ms)")
            Chart(
                chart = lineChart(),
                model = entryModelOf(*latency.mapIndexed { i, v -> i to v }.toTypedArray()),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp)
            )

            SectionLabel("Network Details")
            NetworkDetails(context = context)
        }
    }
}

@Composable
fun NetworkDetails(context: Context) {
    val wifiManager = remember { context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    val info = wifiManager.connectionInfo

    val details = listOf(
        "SSID" to info.ssid,
        "BSSID" to info.bssid,
        "IP Address" to android.text.format.Formatter.formatIpAddress(info.ipAddress),
        "Link Speed" to "${info.linkSpeed} Mbps",
        "Frequency" to "${info.frequency / 1000.0} GHz",
        "RSSI (Signal Strength)" to "${info.rssi} dBm",
        "Network ID" to "${info.networkId}",
        "Supplicant State" to info.supplicantState.name
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        details.forEach { (label, value) ->
            Text(
                text = "$label: $value",
                color = Color.White,
                fontFamily = DidactGothic,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
