package com.tallbreadstick.penspecter.screens.application.penetration

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun DictionaryAttack(navController: NavController? = null) {
    var endpointUrl by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("GET") }
    var headers by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var bodyContent by remember { mutableStateOf("") }
    var expectedResponse by remember { mutableStateOf("") }
    var selectedDictionary by remember { mutableStateOf("Rockyou.txt") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navbar(navController)

        Text(
            text = "Dictionary Attacker",
            fontFamily = DidactGothic,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        // Endpoint Section
        OutlinedTextField(
            value = endpointUrl,
            onValueChange = { endpointUrl = it },
            label = { Text("Endpoint URL", fontFamily = Roboto) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Method Dropdown
        DropdownMenuBox("Method", listOf("GET", "POST", "PUT", "DELETE"), selectedMethod) {
            selectedMethod = it
        }

        // Headers Section
        Text(
            text = "Headers",
            color = Color.White,
            fontFamily = DidactGothic,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        // Add Header Button
        Button(
            onClick = { headers = headers + Pair("Header", "Value") },
            colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "+ Add Header", color = DarkGray, fontFamily = Roboto)
        }

        headers.forEach { header ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = header.first,
                    onValueChange = { headers = headers.map { if (it.first == header.first) it.copy(first = it.first) else it } },
                    label = { Text("Header", fontFamily = Roboto) },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = header.second,
                    onValueChange = { headers = headers.map { if (it.second == header.second) it.copy(second = it.second) else it } },
                    label = { Text("Value", fontFamily = Roboto) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Body Section
        OutlinedTextField(
            value = bodyContent,
            onValueChange = { bodyContent = it },
            label = { Text("Body Content", fontFamily = Roboto) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Expected Response Section
        OutlinedTextField(
            value = expectedResponse,
            onValueChange = { expectedResponse = it },
            label = { Text("Expected Response", fontFamily = Roboto) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Dictionary Section
        Text(
            text = "Dictionary",
            color = Color.White,
            fontFamily = DidactGothic,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        DropdownMenuBox("Select Dictionary", listOf("Rockyou.txt", "SecList.txt"), selectedDictionary) {
            selectedDictionary = it
        }

        // Launch Button
        Button(
            onClick = {
                // Placeholder for the launch functionality
            },
            colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Launch", color = DarkGray, fontFamily = Roboto)
        }
    }
}

// Helper Composable for Dropdown Menu
@Composable
fun DropdownMenuBox(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
    ) {
        Column {
            Text(
                text = "$label: $selectedOption",
                color = Color.White,
                fontFamily = Roboto,
                fontSize = 16.sp
            )
            if (expanded) {
                options.forEach { option ->
                    Text(
                        text = option,
                        color = Color.White,
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                onOptionSelected(option)
                                expanded = false
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
