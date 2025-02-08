package com.tallbreadstick.penspecter.screens.application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray

@Preview
@Composable
fun DeviceDiscovery(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Navbar(navController)
    }
}