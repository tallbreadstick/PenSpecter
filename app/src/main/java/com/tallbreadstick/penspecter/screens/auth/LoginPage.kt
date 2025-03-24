package com.tallbreadstick.penspecter.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.tools.validatePassword
import com.tallbreadstick.penspecter.tools.validateUsername
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun LoginPage(navController: NavController? = null, context: Context? = null) {
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val agree = remember {
        mutableStateOf(false)
    }
    val usernameScroll = rememberScrollState()
    val passwordScroll = rememberScrollState()
    LaunchedEffect(username.value) {
        usernameScroll.animateScrollTo(usernameScroll.maxValue)
    }
    LaunchedEffect(password.value) {
        passwordScroll.animateScrollTo(usernameScroll.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "PenSpecter",
            fontFamily = DidactGothic,
            fontSize = 48.sp,
            color = Color.White,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 80.dp)
        )
        TextField(
            value = username.value,
            maxLines = 1,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = Roboto
            ),
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
            onValueChange = { newText ->
                if (newText.length <= 32) {
                    username.value = newText
                } else {
                    Toast.makeText(context, "Username cannot be more than 32 characters!", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .horizontalScroll(usernameScroll)
        )
        PasswordTextField(password)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agree.value,
                modifier = Modifier.scale(1.2f),
                onCheckedChange = { newState ->
                    agree.value = newState
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
                if (!validateUsername(username.value)) {
                    Toast.makeText(context, "Username contains illegal characters or is too short!", Toast.LENGTH_LONG).show()
                    return@Button
                }
                if (!validatePassword(password.value)) {
                    Toast.makeText(context, "Password contains illegal characters or is too short!", Toast.LENGTH_LONG).show()
                    return@Button
                }
                if (!agree.value) {
                    Toast.makeText(context, "You must agree to the terms and conditions!", Toast.LENGTH_LONG).show()
                    return@Button
                }
                Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_LONG).show()
                navController?.navigate("dashboard") {
                    popUpTo("login_page") { inclusive = true }
                    launchSingleTop = true
                }
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
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                navController?.navigate("register_page") {
                    popUpTo("login_page") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}