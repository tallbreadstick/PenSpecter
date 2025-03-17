package com.tallbreadstick.penspecter.screens.dns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.VeryDarkGray

@Preview
@Composable
fun DNSLookup(navController: NavController? = null) {

    val sidebarOpen = remember { mutableStateOf(false) }
    val queryTypes = listOf("RESOLVE", "REVERSE", "SUBDOMAINS")
    val queryType = remember { mutableStateOf("RESOLVE") }
    val domain = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

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
                Text(result.value, color = Color.White, fontSize = 16.sp)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = domain.value,
                        onValueChange = { domain.value = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontFamily = Roboto),
                        placeholder = {
                            Text("Enter domain or address", fontSize = 16.sp, fontFamily = Roboto, color = Color.LightGray)
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.DarkGray,
                            focusedContainerColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.DarkGray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                            .weight(2f)
                            .clickable { expanded = true }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = queryType.value,
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = Roboto,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            queryTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        queryType.value = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = PaleBlue, contentColor = DarkGray),
                        shape = RectangleShape,
                        onClick = {
                            result.value = "Looking up ${domain.value}..."
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Lookup",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
