package com.tallbreadstick.penspecter.screens.application.penetration

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.ui.theme.*
import com.tallbreadstick.penspecter.viewmodels.PermutationViewModel
import com.tallbreadstick.penspecter.menus.Navbar

@Composable
fun PermutationAttack(
    navController: NavController,
    permutationViewModel: PermutationViewModel,
    context: Context
) {
    var endpointUrl by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("GET") }
    var headers by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var bodyContent by remember { mutableStateOf("") }
    var selectedCharset by remember { mutableStateOf("alphabetic") }
    var maxLength by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Navbar(navController)

            Text(
                text = "Permutation Attacker",
                fontFamily = DidactGothic,
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedDropdown(
                    label = "Method",
                    options = listOf("GET", "POST", "PUT", "DELETE"),
                    selectedOption = selectedMethod,
                    onOptionSelected = {
                        selectedMethod = it
                        permutationViewModel.selectedMethod.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                )

                ThemedOutlinedTextField(
                    value = endpointUrl,
                    onValueChange = {
                        endpointUrl = it
                        permutationViewModel.endpointUrl.value = it
                    },
                    label = "Endpoint URL",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SectionLabel("Headers")

            Button(
                onClick = { headers = headers + Pair("", "") },
                colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 16.dp),
                shape = RectangleShape
            ) {
                Text("+ Add Header", color = DarkGray, fontFamily = Roboto)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                headers.forEachIndexed { index, header ->
                    HeaderRow(
                        header = header,
                        onHeaderChanged = { newPair ->
                            headers = headers.toMutableList().apply { this[index] = newPair }
                            permutationViewModel.headers.value = headers
                        },
                        onRemove = {
                            headers = headers.toMutableList().apply { removeAt(index) }
                            permutationViewModel.headers.value = headers
                        }
                    )
                }
            }

            ThemedOutlinedTextField(
                value = bodyContent,
                onValueChange = {
                    bodyContent = it
                    permutationViewModel.bodyContent.value = it
                },
                label = "Body Content"
            )

            SectionLabel("Charset & Max Length")

            OutlinedDropdown(
                label = "Character Set",
                options = listOf("alphabetic", "numeric", "alphanumeric", "ascii"),
                selectedOption = selectedCharset,
                onOptionSelected = {
                    selectedCharset = it
                    permutationViewModel.selectedCharset.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            ThemedOutlinedTextField(
                value = maxLength,
                onValueChange = {
                    maxLength = it
                    permutationViewModel.maxLength.value = it
                },
                label = "Max Password Length"
            )

            Button(
                onClick = {
                    val validationError = permutationViewModel.validateInputs(
                        endpointUrl, bodyContent, headers, maxLength
                    )

                    if (validationError != null) {
                        Toast.makeText(context, validationError, Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    permutationViewModel.launchPermutationAttack(
                        onMatchFound = {},
                        onFinished = {}
                    )

                    navController.navigate("permutation_attack_in_progress")
                },
                colors = ButtonDefaults.buttonColors(containerColor = PaleBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RectangleShape
            ) {
                Text("Launch", color = DarkGray, fontFamily = Roboto)
            }
        }
    }
}
