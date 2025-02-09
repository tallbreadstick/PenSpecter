package com.tallbreadstick.penspecter.menus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.ui.theme.PaleBlue

@Composable
fun Navbar(navController: NavController?, sidebarOpen: MutableState<Boolean>? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PaleBlue)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (sidebarOpen != null) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        sidebarOpen.value = true
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.burger_menu),
                    contentDescription = "Menu"
                )
            }
        } else {
            Spacer(modifier = Modifier.width(50.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.size(50.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.settings),
                contentDescription = "Settings",
                modifier = Modifier.clickable {
                    navController?.navigate("settings")
                }
            )
        }
    }
}