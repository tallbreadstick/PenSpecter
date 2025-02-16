package com.tallbreadstick.penspecter.screens.application

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.wifi.ScanResult
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tallbreadstick.penspecter.tools.scanWifiNetworks
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun DeviceDiscovery(navController: NavController? = null, context: Context? = null) {
    val sidebarOpen = remember { mutableStateOf(false) }
    val wifiNetworks = remember { mutableStateListOf<ScanResult>() }
    val bluetoothDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val wifiPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                try {
                    scanWifiNetworks(context!!) { results ->
                        wifiNetworks.clear()
                        wifiNetworks.addAll(results)
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Internet Networks",
                fontFamily = DidactGothic,
                fontSize = 24.sp,
                color = Color.White
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(wifiNetworks) { network ->
                    Box(
                        modifier = Modifier
                            .border(2.dp, Color.Gray, shape = RoundedCornerShape(14.dp))
                            .background(Color.DarkGray, shape = RoundedCornerShape(14.dp))
                            .padding(10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = network.SSID.ifEmpty { "Hidden Network" },
                            color = Color.White,
                            fontFamily = Roboto,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Text(
                text = "Bluetooth Devices",
                fontFamily = DidactGothic,
                fontSize = 24.sp,
                color = Color.White
            )
            LazyColumn {
                items(bluetoothDevices) { device ->
                    Text(text = device.name)
                }
            }
        }
    }
}