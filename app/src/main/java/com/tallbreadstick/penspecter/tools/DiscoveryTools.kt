package com.tallbreadstick.penspecter.tools

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat

private const val TAG = "DiscoveryTools"

data class EncryptionStatus(
    val ssid: String,
    val securityType: String
)

fun getSecurityLevel(securityType: String): String {
    return when (securityType.lowercase()) {
        "wpa3", "wpa2", "wpa2/wpa3" -> "Secure"
        "wpa", "wep" -> "At Risk"
        "open" -> "Unsecure"
        else -> "Unknown"
    }
}

fun getSecurityColor(securityType: String): Color {
    return when (securityType.lowercase()) {
        "wpa3", "wpa2", "wpa2/wpa3" -> Color.Green
        "wpa", "wep" -> Color.Yellow
        "open" -> Color.Red
        else -> Color.Gray
    }
}

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun scanWifiNetworks(context: Context, onResults: (List<ScanResult>) -> Unit) {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                val results = wifiManager.scanResults
                Log.d(TAG, "Wi-Fi Scan Results: ${results.size} networks found")
                onResults(results)
                context?.unregisterReceiver(this) // Cleanup receiver
            }
        }
    }

    context.registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    wifiManager.startScan()
    Log.d(TAG, "Started Wi-Fi scan")
}

fun scanBluetoothDevices(context: Context, onDeviceFound: (BluetoothDevice) -> Unit) {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
        Log.w(TAG, "Bluetooth not available or disabled")
        return
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    if (ActivityCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    Log.d(TAG, "Discovered Bluetooth Device: ${it.name ?: "Unknown"} - ${it.address}")
                    onDeviceFound(it)
                }
            } else if (intent?.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                Log.d(TAG, "Bluetooth discovery finished")
                context?.unregisterReceiver(this) // Cleanup receiver
            }
        }
    }

    val filter = IntentFilter().apply {
        addAction(BluetoothDevice.ACTION_FOUND)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    }
    context.registerReceiver(receiver, filter)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
        if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter.startDiscovery()
            Log.d(TAG, "Started Bluetooth discovery (API 31+)")
        } else {
            Log.w(TAG, "Missing BLUETOOTH_SCAN permission")
        }
    } else { // API 28-30
        bluetoothAdapter.startDiscovery()
        Log.d(TAG, "Started Bluetooth discovery (API 28-30)")
    }
}

fun runSecuritySweep(wifiNetworks: List<ScanResult>, onReportGenerated: (List<EncryptionStatus>) -> Unit) {
    val report = mutableListOf<EncryptionStatus>()
    for (network in wifiNetworks) {
        val ssid = network.SSID.ifEmpty { "Hidden Network" }
        val securityType = analyzeSecurityType(network.capabilities)
        when (securityType) {
            "OPEN" -> report.add(EncryptionStatus(ssid, "OPEN"))
            "WEP" -> report.add(EncryptionStatus(ssid, "WEP"))
            "WPA2/WPA3" -> report.add(EncryptionStatus(ssid, "WPA2/WPA3"))
            else -> report.add(EncryptionStatus(ssid, "Unknown"))
        }
    }
    onReportGenerated(report)
}

// Helper function to determine encryption type
private fun analyzeSecurityType(capabilities: String): String {
    return when {
        capabilities.contains("WPA3") || capabilities.contains("WPA2") -> "WPA2/WPA3"
        capabilities.contains("WEP") -> "WEP"
        capabilities.contains("WPA") -> "WPA" // WPA1 is outdated but still better than WEP
        else -> "OPEN" // No security keyword means it's an open network
    }
}
