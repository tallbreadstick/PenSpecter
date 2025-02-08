package com.tallbreadstick.penspecter.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic

@Preview
@Composable
fun SettingsPage(navController: NavController? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Settings",
                fontFamily = DidactGothic,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}