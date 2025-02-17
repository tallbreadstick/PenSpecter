package com.tallbreadstick.penspecter.screens.user

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun UserProfilePage(navController: NavController? = null, context: Context? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hello, User!",
                    fontFamily = DidactGothic,
                    fontSize = 26.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(R.drawable.user_profile),
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Gray, CircleShape)
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: John Doe",
                    fontFamily = Roboto,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = "Email: johndoe@example.com",
                    fontFamily = Roboto,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            navController?.navigate("logout_page")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PaleBlue,
                            contentColor = DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth(0.5f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Log Out",
                            fontFamily = Roboto,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
