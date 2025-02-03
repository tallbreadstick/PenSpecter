package com.tallbreadstick.penspecter.screens.application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray

@Preview
@Composable
fun Dashboard(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Navbar()
    }
}