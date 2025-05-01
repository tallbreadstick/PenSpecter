package com.tallbreadstick.penspecter.screens.application

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.components.ToolIcon
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.viewmodels.SettingsViewModel

@Composable
fun Dashboard(navController: NavController? = null, settingsViewModel: SettingsViewModel) {

    val settings = settingsViewModel.settings.collectAsState().value

    // Function to handle feature navigation and checking if it's enabled
    fun navigateToFeature(featureName: String, route: String) {
        val featureEnabled = when (featureName) {
            "device_discovery" -> settings.deviceDiscovery
            "ping" -> settings.ping
            "wifi_analyzer" -> settings.wifiAnalyzer
            "dns_lookup" -> settings.dnsLookup
            "web_scraper" -> settings.webScraper
            "ip_geolocator" -> settings.ipGeolocator
            "dictionary_attack" -> settings.dictionaryAttack
            "permutation_attack" -> settings.permutationAttack
            else -> false
        }

        if (featureEnabled) {
            navController?.navigate(route)
        } else {
            Toast.makeText(navController?.context, "$featureName is disabled", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Navbar(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .wrapContentSize(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Diagnostic Tools",
                    fontWeight = FontWeight.Bold,
                    fontFamily = DidactGothic,
                    color = Color.White,
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("device_discovery", "device_discovery")
                            },
                        resource = painterResource(R.drawable.device_discovery),
                        title = "Device Discovery"
                    )
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("ping", "ping")
                            },
                        resource = painterResource(R.drawable.traceroute),
                        title = "Ping"
                    )
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("wifi_analyzer", "wifi_analyzer")
                            },
                        resource = painterResource(R.drawable.wifi_analyzer),
                        title = "WiFi Analyzer"
                    )
                }
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("dns_lookup", "dns_lookup")
                            },
                        resource = painterResource(R.drawable.dns_lookup),
                        title = "DNS Lookup"
                    )
                    Box(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f)) // xdd
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Reconnaissance Tools",
                    fontWeight = FontWeight.Bold,
                    fontFamily = DidactGothic,
                    color = Color.White,
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("web_scraper", "web_scraper")
                            },
                        resource = painterResource(R.drawable.web_scraper),
                        title = "Web Scraper"
                    )
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("ip_geolocator", "ip_geolocator")
                            },
                        resource = painterResource(R.drawable.ip_geolocator),
                        title = "IP Geo Locator"
                    )
                    Box(modifier = Modifier.weight(1f))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Penetration Tools",
                    fontWeight = FontWeight.Bold,
                    fontFamily = DidactGothic,
                    color = Color.White,
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("dictionary_attack", "dictionary_attack")
                            },
                        resource = painterResource(R.drawable.dictionary_attack),
                        title = "Dictionary Attack"
                    )
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navigateToFeature("permutation_attack", "permutation_attack")
                            },
                        resource = painterResource(R.drawable.permutation_attack),
                        title = "Permutation Attack"
                    )
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
