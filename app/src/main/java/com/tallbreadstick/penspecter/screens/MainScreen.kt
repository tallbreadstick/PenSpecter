package com.tallbreadstick.penspecter.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tallbreadstick.penspecter.screens.auth.LoginPage
import com.tallbreadstick.penspecter.screens.auth.RecoveryPage
import com.tallbreadstick.penspecter.screens.auth.RegisterPage

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_page", builder = {
        composable("login_page") {
            LoginPage()
        }
        composable("register_page") {
            RegisterPage()
        }
        composable("recovery_page") {
            RecoveryPage()
        }
    })
}