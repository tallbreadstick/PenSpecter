package com.tallbreadstick.penspecter.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun LoginPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Login",
            fontFamily = DidactGothic,
            fontSize = 48.sp,
            color = Color.White,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 80.dp)
        )
        TextField(
            value = "",
            placeholder = {
                Text(
                    text = "Username",
                    fontSize = 20.sp,
                    fontFamily = Roboto,
                    color = Color.LightGray
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.DarkGray,
                focusedContainerColor = Color.DarkGray
            ),
            onValueChange = {

            }
        )
        TextField(
            value = "",
            placeholder = {
                Text(
                    text = "Password",
                    fontSize = 20.sp,
                    fontFamily = Roboto,
                    color = Color.LightGray
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.DarkGray,
                focusedContainerColor = Color.DarkGray
            ),
            onValueChange = {

            }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isChecked = remember {
                mutableStateOf(false)
            }
            Checkbox(
                checked = isChecked.value,
                modifier = Modifier.scale(1.2f),
                onCheckedChange = { newState ->
                    isChecked.value = newState
                }
            )
            Text(
                text = "I agree to PenSpector’s Terms and Conditions and Privacy Policy",
                color = Color.White,
                fontFamily = Roboto,
                fontSize = 12.sp,
                modifier = Modifier.width(200.dp)
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = PaleBlue,
                contentColor = DarkGray
            ),
            shape = RectangleShape,
            onClick = {

            }
        ) {
            Text(
                text = "Sign In",
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Text(
            text = "forgot password?",
            color = Color.Gray,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            fontSize = 16.sp,
            modifier = Modifier.padding(0.dp, 80.dp, 0.dp, 0.dp)
        )
        Text(
            text = "don't have an account? register",
            color = Color.Gray,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            fontSize = 16.sp
        )
    }
}