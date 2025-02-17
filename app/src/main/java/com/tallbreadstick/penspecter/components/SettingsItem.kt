package com.tallbreadstick.penspecter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue

@Composable
fun SettingItem(text: String, checkState: MutableState<Boolean>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontFamily = DidactGothic,
            fontSize = 18.sp,
            color = Color.White
        )
        Switch(
            checked = checkState.value,
            onCheckedChange = {
                checkState.value = !checkState.value
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = PaleBlue,
                checkedTrackColor = Color.LightGray
            )
        )
    }
}