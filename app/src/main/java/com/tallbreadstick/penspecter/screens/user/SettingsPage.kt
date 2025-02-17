package com.tallbreadstick.penspecter.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.components.SettingItem
import com.tallbreadstick.penspecter.components.SettingsSection
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic

@Preview
@Composable
fun SettingsPage(navController: NavController? = null) {

    // diagnostic tools
    val deviceDiscovery = remember {
        mutableStateOf(true)
    }
    val traceroute = remember {
        mutableStateOf(false)
    }
    val packetAnalyzer = remember {
        mutableStateOf(false)
    }
    val dnsLookup = remember {
        mutableStateOf(true)
    }
    val wifiAnalyzer = remember {
        mutableStateOf(true)
    }

    // reconnaissance tools
    val webScraper = remember {
        mutableStateOf(true)
    }
    val ipGeolocator = remember {
        mutableStateOf(true)
    }
    val liveFeeds = remember {
        mutableStateOf(true)
    }

    // penetration tools
    val dictionary = remember {
        mutableStateOf(true)
    }
    val permutation = remember {
        mutableStateOf(true)
    }

    // appearance
    val darkMode = remember {
        mutableStateOf(true)
    }

    // security
    val vpn = remember {
        mutableStateOf(false)
    }
    val root = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Settings",
                fontFamily = DidactGothic,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "Diagnostic Tools") {
                SettingItem(text = "Enable Device Discovery", checkState = deviceDiscovery)
                SettingItem(text = "Enable Traceroute", checkState = traceroute)
                SettingItem(text = "Enable Packet Analyzer", checkState = packetAnalyzer)
                SettingItem(text = "Enable DNS Lookup", checkState = dnsLookup)
                SettingItem(text = "Enable WiFi Analyzer", checkState = wifiAnalyzer)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Reconnaissance Tools") {
                SettingItem(text = "Enable Web Scraper", checkState = webScraper)
                SettingItem(text = "Enable IP Geolocator", checkState = ipGeolocator)
                SettingItem(text = "Enable Live Feeds", checkState = liveFeeds)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Penetration Tools") {
                SettingItem(text = "Enable Dictionary Attack", checkState = dictionary)
                SettingItem(text = "Enable Permutation Attack", checkState = permutation)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Appearance") {
                SettingItem(text = "Enable Dark Mode", checkState = darkMode)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Security") {
                SettingItem(text = "Enable VPN", checkState = vpn)
                SettingItem(text = "Enable Root", checkState = root)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "visit developer page",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        navController?.navigate("developer_page")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}