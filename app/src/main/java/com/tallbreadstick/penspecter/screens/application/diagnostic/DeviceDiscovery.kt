package com.tallbreadstick.penspecter.screens.application.diagnostic

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.wifi.ScanResult
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.tools.EncryptionStatus
import com.tallbreadstick.penspecter.tools.getSecurityColor
import com.tallbreadstick.penspecter.tools.getSecurityLevel
import com.tallbreadstick.penspecter.tools.runSecuritySweep
import com.tallbreadstick.penspecter.tools.scanWifiNetworks
import com.tallbreadstick.penspecter.tools.scanBluetoothDevices
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun DeviceDiscovery(navController: NavController? = null, context: Context? = null) {
    val sidebarOpen = remember { mutableStateOf(false) }
    val wifiNetworks = remember { mutableStateListOf<ScanResult>() }
    val bluetoothDevices = remember { mutableStateListOf<BluetoothDevice>() }

    var securityReport by remember { mutableStateOf(listOf<EncryptionStatus>()) }
    var showDialog by remember { mutableStateOf(false) } // Controls popup visibility

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
                    Log.d("PenSpecter", "No permission provided for WiFi")
                    Toast.makeText(context, "No permission provided for WiFi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val bluetoothPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.BLUETOOTH_SCAN] == true &&
                    permissions[Manifest.permission.BLUETOOTH_ADMIN] == true

            if (granted) {
                try {
                    scanBluetoothDevices(context!!) { device ->
                        if (!bluetoothDevices.contains(device)) {
                            bluetoothDevices.add(device)
                        }
                    }
                } catch (e: SecurityException) {
                    Log.d("PenSpecter", "No permission provided for Bluetooth")
                    Toast.makeText(context, "No permission provided for Bluetooth", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        wifiPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        bluetoothPermissionRequest.launch(
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_ADMIN)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navbar(navController, sidebarOpen)

        Text(
            text = "Device Discovery",
            fontFamily = DidactGothic,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SectionTitle("Nearby WiFi Networks") }
            items(wifiNetworks) { network ->
                DeviceCard(network.SSID.ifEmpty { "Hidden Network" })
            }
            item { SectionTitle("Nearby Bluetooth Devices") }
            items(bluetoothDevices) { device ->
                DeviceCard(device.name ?: "Unknown Device")
            }
        }

        // 🔹 Run Security Sweep Button
        Button(
            onClick = {
                runSecuritySweep(wifiNetworks) { report ->
                    securityReport = report
                    showDialog = true // Open popup
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PaleBlue,
                contentColor = DarkGray
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Run Security Sweep",
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }

    // 🔹 Security Report Popup Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Security Report",
                    fontFamily = DidactGothic,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF424242), shape = RoundedCornerShape(12.dp)) // Dark gray background
                        .padding(8.dp)
                ) {
                    LazyColumn {
                        items(securityReport) { status ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = status.ssid,
                                    color = Color.White,
                                    fontFamily = Roboto,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(2f)
                                )
                                Text(
                                    text = status.securityType,
                                    color = Color.White,
                                    fontFamily = Roboto,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = getSecurityLevel(status.securityType),
                                    color = getSecurityColor(status.securityType),
                                    fontFamily = Roboto,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PaleBlue,
                        contentColor = DarkGray
                    ),
                    shape = RectangleShape
                ) {
                    Text("Close",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            containerColor = Color(0xFF424242),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontFamily = DidactGothic,
        fontSize = 24.sp,
        color = Color.White,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun DeviceCard(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(14.dp))
            .background(Color.DarkGray, shape = RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Text(
            text = name,
            color = Color.White,
            fontFamily = Roboto,
            fontSize = 16.sp
        )
    }
}
