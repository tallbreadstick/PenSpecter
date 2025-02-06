package com.tallbreadstick.penspecter.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tallbreadstick.penspecter.ui.theme.LightGray
import com.tallbreadstick.penspecter.ui.theme.PaleBlue

@Composable
fun Navbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PaleBlue)
            .padding(8.dp)
            .wrapContentSize(Alignment.CenterEnd)
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "uh")
        }
    }
}