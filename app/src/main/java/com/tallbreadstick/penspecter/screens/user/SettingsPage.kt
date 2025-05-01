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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.tallbreadstick.penspecter.viewmodels.SettingsViewModel

@Preview
@Composable
fun SettingsPage(navController: NavController? = null, viewModel: SettingsViewModel? = null) {

    val settings by viewModel!!.settings.collectAsState()

    // diagnostic tools
    val featureSettings = mapOf(
        "device_discovery" to remember { mutableStateOf(settings.deviceDiscovery) },
        "ping" to remember { mutableStateOf(settings.ping) },
        "dns_lookup" to remember { mutableStateOf(settings.dnsLookup) },
        "wifi_analyzer" to remember { mutableStateOf(settings.wifiAnalyzer) },
        "web_scraper" to remember { mutableStateOf(settings.webScraper) },
        "ip_geolocator" to remember { mutableStateOf(settings.ipGeolocator) },
        "dictionary_attack" to remember { mutableStateOf(settings.dictionaryAttack) },
        "permutation_attack" to remember { mutableStateOf(settings.permutationAttack) }
    )

    LaunchedEffect(navController) {
        navController?.addOnDestinationChangedListener { _, _, _ ->
            viewModel!!.updateSetting { currentSettings ->
                currentSettings.copy(
                    deviceDiscovery = featureSettings["device_discovery"]!!.value,
                    ping = featureSettings["ping"]!!.value,
                    dnsLookup = featureSettings["dns_lookup"]!!.value,
                    wifiAnalyzer = featureSettings["wifi_analyzer"]!!.value,
                    webScraper = featureSettings["web_scraper"]!!.value,
                    ipGeolocator = featureSettings["ip_geolocator"]!!.value,
                    dictionaryAttack = featureSettings["dictionary_attack"]!!.value,
                    permutationAttack = featureSettings["permutation_attack"]!!.value
                )
            }
        }
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
                SettingItem(text = "Enable Device Discovery", checkState = featureSettings["device_discovery"]!!)
                SettingItem(text = "Enable Ping", checkState = featureSettings["ping"]!!)
                SettingItem(text = "Enable DNS Lookup", checkState = featureSettings["dns_lookup"]!!)
                SettingItem(text = "Enable WiFi Analyzer", checkState = featureSettings["wifi_analyzer"]!!)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Reconnaissance Tools") {
                SettingItem(text = "Enable Web Scraper", checkState = featureSettings["web_scraper"]!!)
                SettingItem(text = "Enable IP Geolocator", checkState = featureSettings["ip_geolocator"]!!)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Penetration Tools") {
                SettingItem(text = "Enable Dictionary Attack", checkState = featureSettings["dictionary_attack"]!!)
                SettingItem(text = "Enable Permutation Attack", checkState = featureSettings["permutation_attack"]!!)
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