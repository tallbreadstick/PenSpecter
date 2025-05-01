package com.tallbreadstick.penspecter.screens.application.penetration

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto
import com.tallbreadstick.penspecter.viewmodels.DictionaryViewModel
import com.tallbreadstick.penspecter.menus.Navbar

@Composable
fun DictionaryAttack(
    navController: NavController,
    dictionaryViewModel: DictionaryViewModel,
    context: Context
) {
    var endpointUrl by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("GET") }
    var headers by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var bodyContent by remember { mutableStateOf("") }
    var selectedDictionary by remember { mutableStateOf("rockyou.txt") }
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
                text = "Dictionary Attacker",
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
                        dictionaryViewModel.selectedMethod.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                )

                ThemedOutlinedTextField(
                    value = endpointUrl,
                    onValueChange = {
                        endpointUrl = it
                        dictionaryViewModel.endpointUrl.value = it
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
                            dictionaryViewModel.headers.value = headers
                        },
                        onRemove = {
                            headers = headers.toMutableList().apply { removeAt(index) }
                            dictionaryViewModel.headers.value = headers
                        }
                    )
                }
            }

            ThemedOutlinedTextField(
                value = bodyContent,
                onValueChange = {
                    bodyContent = it
                    dictionaryViewModel.bodyContent.value = it
                },
                label = "Body Content"
            )

            SectionLabel("Dictionary")

            OutlinedDropdown(
                label = "Select Dictionary",
                options = listOf("rockyou.txt"),
                selectedOption = selectedDictionary,
                onOptionSelected = {
                    selectedDictionary = it
                    dictionaryViewModel.selectedDictionary.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Button(
                onClick = {
                    val validationError = dictionaryViewModel.validateInputs(
                        endpointUrl, bodyContent, headers
                    )

                    if (validationError != null) {
                        Toast.makeText(context, validationError, Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    Log.d("DICTIONARY", "Validation passed, starting attack...")
                    dictionaryViewModel.launchDictionaryAttack(
                        onMatchFound = { matchedPassword ->
                            Log.d("DICTIONARY", "MATCH callback: $matchedPassword")
                        },
                        onFinished = {
                            Log.d("DICTIONARY", "Attack finished")
                        }
                    )

                    navController.navigate("dictionary_attack_in_progress")
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
@Composable
fun HeaderRow(
    header: Pair<String, String>,
    onHeaderChanged: (Pair<String, String>) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemedOutlinedTextField(
            value = header.first,
            onValueChange = { onHeaderChanged(it to header.second) },
            label = "Header",
            modifier = Modifier.weight(1f)
        )
        ThemedOutlinedTextField(
            value = header.second,
            onValueChange = { onHeaderChanged(header.first to it) },
            label = "Value",
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove header",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun ThemedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = Roboto) },
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontFamily = DidactGothic,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun OutlinedDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, fontFamily = Roboto) },
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
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(DarkGray)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White, fontFamily = Roboto) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
