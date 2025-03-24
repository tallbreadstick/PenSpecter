package com.tallbreadstick.penspecter.screens.application.reconnaissance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.components.OpenStreetMapView
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.DidactGothic

@Preview
@Composable
fun IPGeolocator(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    // State to store latitude and longitude
    var latitude by remember { mutableDoubleStateOf(37.7749) }  // Default: San Francisco
    var longitude by remember { mutableDoubleStateOf(-122.4194) }

    Column(modifier = Modifier.fillMaxSize()) {
        Navbar(navController)

        // Search Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 20.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(
                        width = if (isFocused) 2.dp else 0.dp,
                        color = if (isFocused) PaleBlue else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = TextStyle(
                            fontFamily = DidactGothic,
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .onFocusChanged { isFocused = it.isFocused },
                        placeholder = { Text("Enter IP Address or Domain", fontFamily = DidactGothic, color = Color.Gray) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                    Button(
                        onClick = {
                            // TODO: Call IP geolocation API here
                            // Example: If API returns latitude = 40.7128, longitude = -74.0060
                            latitude = 40.7128  // New York example
                            longitude = -74.0060
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
                        modifier = Modifier.size(50.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.dns_lookup),
                            contentDescription = "Search"
                        )
                    }
                }
            }
        }

        // OpenStreetMap
        Box(modifier = Modifier.fillMaxSize()) {
            OpenStreetMapView(latitude, longitude)
        }
    }
}
