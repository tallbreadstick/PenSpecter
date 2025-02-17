package com.tallbreadstick.penspecter.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun LogoutPage(navController: NavController? = null, context: Context? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .wrapContentSize(Alignment.Center),
       verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(36.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Are you sure you want to log out?",
                fontFamily = DidactGothic,
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 80.dp)
                    .fillMaxWidth(0.6f)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PaleBlue,
                        contentColor = DarkGray
                    ),
                    shape = RectangleShape,
                    onClick = {
                        Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_LONG).show()
                        navController?.navigate("login_page") {
                            popUpTo("logout_page") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Yes",
                        fontFamily = Roboto,
                        fontSize = 20.sp
                    )
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PaleBlue,
                        contentColor = DarkGray
                    ),
                    shape = RectangleShape,
                    onClick = {
                        navController?.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "No",
                        fontFamily = Roboto,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.weight(0.2f))
            }
        }
    }
}
