package com.tallbreadstick.penspecter.screens.application.reconnaissance

import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.viewmodels.GeolocatorViewModel

@Composable
@Preview
fun IPGeolocator(navController: NavController? = null) {

    val context = LocalContext.current
    val mapStyleOptions = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_map_style)
    }
    val properties = remember {
        MapProperties(mapStyleOptions = mapStyleOptions)
    }

    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val viewModel: GeolocatorViewModel = viewModel()

    val city by viewModel.city.collectAsState()
    val country by viewModel.country.collectAsState()
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()

    val markerState = remember { MarkerState(position = LatLng(latitude, longitude)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 10f)
    }

// Trigger marker & camera position change when ViewModel updates
    LaunchedEffect(latitude, longitude) {
        val target = LatLng(latitude, longitude)
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(target, 10f),
            durationMs = 2000
        )
        markerState.position = target
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Navbar(navController)

        // Map + Search Overlay
        Box(modifier = Modifier.fillMaxSize()) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                Marker(
                    state = markerState,
                    title = "Location",
                    snippet = "Lat: $latitude, Lng: $longitude"
                )
            }

            if (city.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp)
                        .background(
                            Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Approximated Location: $city, $country",
                        color = Color.White,
                        fontFamily = DidactGothic,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp) // Adjust this height as needed
                    .align(Alignment.BottomCenter)
            )

            // Overlaid Search Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 64.dp)
                    .align(Alignment.BottomCenter) // Float at top of map
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
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
                        placeholder = { Text("Enter IP Address", fontFamily = DidactGothic, color = Color.Gray) },
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
                            viewModel.searchIP(searchQuery)
                        }
                        ,
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
    }
}