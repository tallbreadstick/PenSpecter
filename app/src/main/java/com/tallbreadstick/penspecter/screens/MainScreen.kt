package com.tallbreadstick.penspecter.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tallbreadstick.penspecter.screens.application.Dashboard
import com.tallbreadstick.penspecter.screens.application.diagnostic.DeviceDiscovery
import com.tallbreadstick.penspecter.screens.auth.LoginPage
import com.tallbreadstick.penspecter.screens.auth.LogoutPage
import com.tallbreadstick.penspecter.screens.auth.RecoveryPage
import com.tallbreadstick.penspecter.screens.auth.RegisterPage
import com.tallbreadstick.penspecter.screens.application.diagnostic.DNSLookup
import com.tallbreadstick.penspecter.screens.application.diagnostic.Ping
import com.tallbreadstick.penspecter.screens.application.diagnostic.WifiAnalyzer
import com.tallbreadstick.penspecter.screens.application.penetration.DictionaryAttack
import com.tallbreadstick.penspecter.screens.application.penetration.DictionaryAttackInProgress
import com.tallbreadstick.penspecter.screens.application.penetration.PermutationAttack
import com.tallbreadstick.penspecter.screens.application.penetration.PermutationAttackInProgress
import com.tallbreadstick.penspecter.screens.application.reconnaissance.IPGeolocator
import com.tallbreadstick.penspecter.screens.application.reconnaissance.WebScraper
import com.tallbreadstick.penspecter.screens.user.DeveloperPage
import com.tallbreadstick.penspecter.screens.user.SettingsPage
import com.tallbreadstick.penspecter.screens.user.UserProfilePage
import com.tallbreadstick.penspecter.tools.DatabaseProvider
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.viewmodels.DNSViewModel
import com.tallbreadstick.penspecter.viewmodels.DictionaryViewModel
import com.tallbreadstick.penspecter.viewmodels.PermutationViewModel
import com.tallbreadstick.penspecter.viewmodels.ScraperViewModel
import com.tallbreadstick.penspecter.viewmodels.SettingsViewModel

@Composable
fun MainScreen() {
    val db = DatabaseProvider.getDatabase(LocalContext.current)
    val userDao = db.userDao()
    val userCount = remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        userCount.value = userDao.getUserCount()
    }

    val startDestination = if (userCount.value > 0) "login_page" else "register_page"
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = viewModel()
    val dnsViewModel: DNSViewModel = viewModel()
    val scraperViewModel: ScraperViewModel = viewModel()
    val dictionaryViewModel: DictionaryViewModel = viewModel()
    val permutationViewModel: PermutationViewModel = viewModel()
    Surface(color = DarkGray) {
        NavHost(navController = navController, startDestination = startDestination, builder = {

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
                Dashboard(navController, settingsViewModel)
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

            // Diagnostic Tools
            composable("device_discovery") {
                DeviceDiscovery(navController, LocalContext.current)
            }
            composable("ping") {
                Ping(navController)
            }
            composable("dns_lookup") {
                DNSLookup(navController, dnsViewModel)
            }
            composable("wifi_analyzer") {
                WifiAnalyzer(navController)
            }

            // Reconnaissance Tools
            composable("web_scraper") {
                WebScraper(navController, scraperViewModel, LocalContext.current)
            }
            composable("ip_geolocator") {
                IPGeolocator(navController)
            }

            // Penetration Tools
            composable("dictionary_attack") {
                DictionaryAttack(navController, dictionaryViewModel, LocalContext.current)
            }
            composable("dictionary_attack_in_progress") {
                DictionaryAttackInProgress(navController, dictionaryViewModel)
            }
            composable("permutation_attack") {
                PermutationAttack(navController, permutationViewModel, LocalContext.current)
            }
            composable("permutation_attack_in_progress") {
                PermutationAttackInProgress(navController, permutationViewModel)
            }

        })
    }
}