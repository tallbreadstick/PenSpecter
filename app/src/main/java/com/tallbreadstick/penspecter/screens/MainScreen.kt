package com.tallbreadstick.penspecter.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tallbreadstick.penspecter.screens.application.Dashboard
import com.tallbreadstick.penspecter.screens.application.DeviceDiscovery
import com.tallbreadstick.penspecter.screens.auth.LoginPage
import com.tallbreadstick.penspecter.screens.auth.LogoutPage
import com.tallbreadstick.penspecter.screens.auth.RecoveryPage
import com.tallbreadstick.penspecter.screens.auth.RegisterPage
import com.tallbreadstick.penspecter.screens.dns.DNSLookup
import com.tallbreadstick.penspecter.screens.user.DeveloperPage
import com.tallbreadstick.penspecter.screens.user.SettingsPage
import com.tallbreadstick.penspecter.screens.user.UserProfilePage
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.viewmodels.SettingsViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = viewModel()
    Surface(color = DarkGray) {
        NavHost(navController = navController, startDestination = "login_page", builder = {

            // Auth Pages
            composable("login_page") {
                LoginPage(navController, LocalContext.current)
            }
            composable("register_page") {
                RegisterPage(navController, LocalContext.current)
            }
            composable("logout_page") {
                LogoutPage(navController, LocalContext.current)
            }
            composable("recovery_page") {
                RecoveryPage(navController)
            }

            // Control Pages
            composable("dashboard") {
                Dashboard(navController)
            }
            composable("settings") {
                SettingsPage(navController, settingsViewModel)
            }
            composable("user_profile") {
                UserProfilePage(navController, LocalContext.current)
            }
            composable("developer_page") {
                DeveloperPage(navController)
            }

            // Feature Pages
            composable("device_discovery") {
                DeviceDiscovery(navController, LocalContext.current)
            }
            composable("dns_lookup") {
                DNSLookup(navController)
            }

        })
    }
}