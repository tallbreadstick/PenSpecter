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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar

@Preview
@Composable
fun DNSLookup(navController: NavController? = null) {

    val sidebarOpen = remember { mutableStateOf(false) }
    val queryType = remember { mutableStateOf("A") }
    val domain = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("Fetching results...") }
    val scrollState = rememberScrollState()

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
                    .background(Color.Black, RoundedCornerShape(8.dp))
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
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.DarkGray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                            .clickable { /* Expand dropdown logic */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
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
                    }

                    TextField(
                        value = domain.value,
                        onValueChange = { domain.value = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontFamily = Roboto),
                        placeholder = {
                            Text("Enter domain", fontSize = 16.sp, fontFamily = Roboto, color = Color.LightGray)
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.DarkGray,
                            focusedContainerColor = Color.DarkGray
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = PaleBlue, contentColor = DarkGray),
                        shape = RectangleShape,
                        onClick = {
                            result.value = "Looking up ${domain.value}..."
                        }
                    ) {
                        Text("Lookup", fontFamily = Roboto, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
