package com.tallbreadstick.penspecter.screens.application.diagnostic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto
import kotlinx.coroutines.launch
import runPing

@Preview
@Composable
fun Ping(navController: NavController? = null) {

    val targetHost = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navbar(navController)

        Text(
            text = "Ping Utility",
            fontFamily = Roboto,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Status/result centered in available space
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading.value -> {
                println("Loading...")
                CircularProgressIndicator(color = Color.White)
            }
                result.value != null -> {
                Text(
                    text = result.value ?: "",
                    fontSize = 32.sp,
                    fontFamily = Roboto,
                    color = if (result.value!!.contains("ms")) Color.Green else Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            }

        }

        // Input and button pinned to bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = targetHost.value,
                onValueChange = { targetHost.value = it },
                label = { Text("Enter host or IP", fontFamily = Roboto) },
                textStyle = TextStyle(color = Color.White, fontFamily = Roboto),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DarkGray,
                    unfocusedContainerColor = DarkGray,
                    focusedIndicatorColor = PaleBlue,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = PaleBlue,
                    focusedLabelColor = PaleBlue,
                    unfocusedLabelColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    result.value = null
                    isLoading.value = true
                    coroutineScope.launch {
                        isLoading.value = true
                        val output = runPing(targetHost.value)
                        result.value = output
                        isLoading.value = false
                    }
                },
                enabled = !isLoading.value,
                colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape
            ) {
                Text(
                    "Ping",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkGray
                )
            }
        }
    }
}
