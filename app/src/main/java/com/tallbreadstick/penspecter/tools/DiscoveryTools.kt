package com.tallbreadstick.penspecter.tools

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission

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

