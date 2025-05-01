package com.tallbreadstick.penspecter.screens.application.diagnostic

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.screens.application.penetration.SectionLabel
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import kotlinx.coroutines.delay
import kotlin.random.Random

@Preview
@Composable
fun WifiAnalyzer(navController: NavController? = null) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val wifiManager = remember {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    var throughput by remember { mutableStateOf(List(50) { wifiManager.connectionInfo.linkSpeed }) }
    var latency by remember { mutableStateOf(List(50) { rssiToLatency(wifiManager.connectionInfo.rssi) }) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val info = wifiManager.connectionInfo
            throughput = (throughput.drop(1) + Random.nextInt(30, 100)).toList()
            latency = (latency.drop(1) + rssiToLatency(info.rssi)).toList()
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
            ChartBox(
                values = throughput,
                label = "Throughput",
                minY = 0f,
                maxY = 150f
            )

            SectionLabel("Latency (ms)")
            ChartBox(
                values = latency,
                label = "Latency",
                minY = 0f,
                maxY = 300f
            )

            SectionLabel("Network Details")
            NetworkDetails(context)
        }
    }
}

@Composable
fun ChartBox(values: List<Int>, label: String, minY: Float, maxY: Float) {
    val chartScrollState = rememberChartScrollState()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "$label Over Time",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = DidactGothic,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val entries = values.mapIndexed { index, value ->
                FloatEntry(index.toFloat(), value.toFloat())
            }

            val model = entryModelOf(entries)

            Chart(
                chart = lineChart(),
                model = model,
                startAxis = startAxis(),
                bottomAxis = rememberBottomAxis(),
                chartScrollState = chartScrollState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun NetworkDetails(context: Context) {
    val wifiManager = remember {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    val info = wifiManager.connectionInfo

    val details = listOf(
        "SSID" to info.ssid,
        "BSSID" to info.bssid,
        "IP Address" to Formatter.formatIpAddress(info.ipAddress),
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

fun rssiToLatency(rssi: Int): Int {
    return (100 - rssi).coerceIn(10, 300)
}
