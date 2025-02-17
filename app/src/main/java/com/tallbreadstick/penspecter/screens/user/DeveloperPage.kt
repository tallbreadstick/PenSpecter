package com.tallbreadstick.penspecter.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.ui.theme.PaleBlue

@Preview
@Composable
fun DeveloperPage(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.bread),
            contentDescription = "Developer Image",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp),
        )

        Text(
            text = "Developer's GitHub:",
            textAlign = TextAlign.Center,
            style = TextStyle(color = Color.White, fontSize = 18.sp),
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "https://github.com/tallbreadstick",
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            style = TextStyle(color = PaleBlue, fontSize = 18.sp),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
