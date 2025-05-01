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
import com.tallbreadstick.penspecter.viewmodels.PermutationViewModel

@Composable
fun PermutationAttackInProgress(
    navController: NavController,
    viewModel: PermutationViewModel
) {
    val currentPassword = viewModel.currentPassword.collectAsState().value
    val match = viewModel.matchFound.collectAsState().value
    val isFinished = viewModel.isFinished.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                match != null -> {
                    Text(
                        text = "Match found: $match",
                        fontFamily = DidactGothic,
                        fontSize = 22.sp,
                        color = PaleBlue
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.popBackStack() },
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
                isFinished -> {
                    Text(
                        text = "Attack completed.\nNo match found.",
                        fontFamily = DidactGothic,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.popBackStack() },
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
                else -> {
                    CircularProgressIndicator(color = PaleBlue, strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Running permutation attack...",
                        fontFamily = DidactGothic,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Trying: ${currentPassword ?: "..." }",
                        fontFamily = Roboto,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.cancelAttack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RectangleShape
                    ) {
                        Text(
                            "Stop",
                            fontFamily = Roboto,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
