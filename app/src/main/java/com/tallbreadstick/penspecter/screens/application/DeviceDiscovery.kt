package com.tallbreadstick.penspecter.screens.application

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic

@Preview
@Composable
fun DeviceDiscovery(navController: NavController? = null, context: Context? = null) {
    val sidebarOpen = remember { mutableStateOf(false) }
    val wifiDevices = remember { mutableStateListOf<ScanResult>() }
    val bluetoothDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val wifiPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                try {
                    scanWifiNetworks(context!!) { results ->
                        wifiDevices.clear()
                        wifiDevices.addAll(results)
                    }
                } catch (e: SecurityException) {
                    Log.d("PenSpecter", "No permission provided")
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        wifiPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Navbar(navController, sidebarOpen)
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Internet Devices",
                fontFamily = DidactGothic,
                fontSize = 24.sp,
                color = Color.White
            )
            LazyColumn {
                items(wifiDevices) { device ->
                    Text(text = device.SSID.ifEmpty { "Hidden Network" })
                }
            }
            Text(
                text = "Bluetooth Devices",
                fontFamily = DidactGothic,
                fontSize = 24.sp,
                color = Color.White
            )
            Column {

            }
        }
    }
}

@RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
fun scanWifiNetworks(context: Context, onResults: (List<ScanResult>) -> Unit) {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    context.registerReceiver(
        object : BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    val results = wifiManager.scanResults
                    onResults(results)
                }
            }
        },
        IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    )
    wifiManager.startScan()
}