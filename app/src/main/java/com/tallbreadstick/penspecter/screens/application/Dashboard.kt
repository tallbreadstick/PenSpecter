package com.tallbreadstick.penspecter.screens.application

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.components.ToolIcon
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic

@Preview
@Composable
fun Dashboard(navController: NavController? = null) {
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
                                navController?.navigate("device_discovery")
                            },
                        resource = painterResource(R.drawable.device_discovery),
                        title = "Device Discovery"
                    )
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController?.navigate("traceroute")
                            },
                        resource = painterResource(R.drawable.traceroute),
                        title = "Traceroute"
                    )
                    ToolIcon(
                        modifier = Modifier.weight(1f),
                        resource = painterResource(R.drawable.packet_analyzer),
                        title = "Packet Analyzer"
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
                                navController?.navigate("dns_lookup")
                            },
                        resource = painterResource(R.drawable.dns_lookup),
                        title = "DNS Lookup"
                    )
                    ToolIcon(
                        modifier = Modifier.weight(1f),
                        resource = painterResource(R.drawable.wifi_analyzer),
                        title = "WiFi Analyzer"
                    )
                    Box(modifier = Modifier.weight(1f)) {} // xdd
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
                        modifier = Modifier.weight(1f),
                        resource = painterResource(R.drawable.web_scraper),
                        title = "Web Scraper"
                    )
                    ToolIcon(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController?.navigate("ip_geolocator")
                            },
                        resource = painterResource(R.drawable.ip_geolocator),
                        title = "IP Geo Locator"
                    )
                    ToolIcon(
                        modifier = Modifier.weight(1f),
                        resource = painterResource(R.drawable.live_feeds),
                        title = "Live Feeds"
                    )
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
                        modifier = Modifier.weight(1f),
                        resource = painterResource(R.drawable.dictionary_attack),
                        title = "Dictionary Attack"
                    )
                    ToolIcon(
                        modifier = Modifier.weight(1f),
                        resource = painterResource(R.drawable.permutation_attack),
                        title = "Permutation Attack"
                    )
                    Box(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}