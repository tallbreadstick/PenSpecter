package com.tallbreadstick.penspecter.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun PasswordTextField(password: MutableState<String>) {
    var showPassword by remember { mutableStateOf(false) }

    TextField(
        value = password.value,
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp
        ),
        placeholder = {
            Text(
                text = "Password",
                fontSize = 20.sp,
                color = Color.LightGray
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.DarkGray,
            focusedContainerColor = Color.DarkGray
        ),
        onValueChange = { newText ->
            if (newText.length <= 32) {
                password.value = newText
            }
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(imageVector = image, contentDescription = "Toggle Password Visibility", tint = Color.White)
            }
        },
        modifier = Modifier.fillMaxWidth(0.75f)
    )
}
