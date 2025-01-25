package com.tallbreadstick.penspecter.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tallbreadstick.penspecter.screens.auth.LoginPage
import com.tallbreadstick.penspecter.screens.auth.RecoveryPage
import com.tallbreadstick.penspecter.screens.auth.RegisterPage
import com.tallbreadstick.penspecter.ui.theme.DarkGray

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Surface(color = DarkGray) {
        NavHost(navController = navController, startDestination = "login_page", builder = {
            composable("login_page") {
                LoginPage(navController)
            }
            composable("register_page") {
                RegisterPage(navController)
            }
            composable("recovery_page") {
                RecoveryPage()
            }
        })
    }
}