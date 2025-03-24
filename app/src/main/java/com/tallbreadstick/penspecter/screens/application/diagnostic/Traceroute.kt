package com.tallbreadstick.penspecter.screens.application.diagnostic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.tools.runTraceroute
import com.tallbreadstick.penspecter.ui.theme.*
import kotlinx.coroutines.launch

@Preview
@Composable
fun Traceroute(navController: NavController? = null) {

    val sidebarOpen = remember { mutableStateOf(false) }
    val targetHost = remember { mutableStateOf("") }
    val resultList = remember { mutableStateListOf<String>() }
    val scrollState = rememberScrollState()
    val isLoading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(resultList.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Navbar(navController, sidebarOpen)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .background(VeryDarkGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                if (resultList.isNotEmpty()) {
                    StyledTracerouteResult(resultList)
                } else {
                    Text(
                        if (isLoading.value) "Tracing route..." else "No Results",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = targetHost.value,
                    onValueChange = { targetHost.value = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontFamily = Roboto),
                    placeholder = {
                        Text(
                            "Enter target host or IP",
                            fontSize = 16.sp,
                            fontFamily = Roboto,
                            color = Color.LightGray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.DarkGray,
                        focusedContainerColor = Color.DarkGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        resultList.clear()
                        isLoading.value = true
                        coroutineScope.launch {
                            runTraceroute(targetHost.value, { result ->
                                resultList.add(result)
                            }, isLoading)
                        }
                    }
                ) {
                    Text(
                        "Start Traceroute",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StyledTracerouteResult(results: List<String>) {
    Column {
        results.forEachIndexed { index, hop ->
            Text(
                "$index: $hop",
                color = when {
                    hop.contains("Failed", ignoreCase = true) || hop.contains("Error") -> Color.Red
                    hop.contains("*") -> Color.Yellow // Timeouts
                    else -> Color.Green
                },
                fontSize = 16.sp
            )
        }
    }
}