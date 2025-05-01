package com.tallbreadstick.penspecter.screens.application.penetration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto
import com.tallbreadstick.penspecter.viewmodels.DictionaryViewModel

@Composable
fun DictionaryAttackInProgress(
    navController: NavController,
    viewModel: DictionaryViewModel
) {
    val attackStatus = viewModel.attackStatus.collectAsState().value
    val progress = viewModel.progress.collectAsState().value
    val total = viewModel.total.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (attackStatus) {
                "Running" -> {
                    CircularProgressIndicator(
                        color = PaleBlue,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Running dictionary attack...",
                        fontFamily = DidactGothic,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    if (total > 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                        LinearProgressIndicator(
                            progress = { progress / total.toFloat() },
                            color = PaleBlue,
                            trackColor = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$progress/$total",
                            fontFamily = Roboto,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
                "Idle" -> {
                    Text(
                        text = "Attack not started yet",
                        fontFamily = DidactGothic,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                else -> {
                    Text(
                        text = attackStatus,
                        fontFamily = DidactGothic,
                        fontSize = 22.sp,
                        color = PaleBlue
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
                        shape = RectangleShape
                    ) {
                        Text(
                            "Go Back",
                            fontFamily = Roboto,
                            color = DarkGray
                        )
                    }
                }
            }
        }
    }
}
