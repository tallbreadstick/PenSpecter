package com.tallbreadstick.penspecter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tallbreadstick.penspecter.ui.theme.DidactGothic

@Composable
fun ToolIcon(modifier: Modifier, resource: Painter, title: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .border(4.dp, Color.Gray, shape = RoundedCornerShape(14.dp))
                .background(Color.DarkGray, shape = RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Image(
                painter = resource,
                colorFilter = ColorFilter.tint(Color.Gray),
                contentDescription = title
            )
        }
        Text(
            text = title,
            fontFamily = DidactGothic,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp)
        )
    }
}