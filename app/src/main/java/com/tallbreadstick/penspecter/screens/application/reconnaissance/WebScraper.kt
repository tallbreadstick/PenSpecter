package com.tallbreadstick.penspecter.screens.application.reconnaissance

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.DidactGothic
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto

@Preview
@Composable
fun WebScraper(navController: NavController? = null, context: Context? = null) {
    val sidebarOpen = remember { mutableStateOf(false) }
    var urlInput by remember { mutableStateOf("") }

    val scrapedTables = remember { mutableStateListOf<String>() }
    val scrapedLists = remember { mutableStateListOf<String>() }
    val scrapedForms = remember { mutableStateListOf<String>() }
    val scrapedMedia = remember { mutableStateListOf<String>() }

    val expandedTables = remember { mutableStateOf(false) }
    val expandedLists = remember { mutableStateOf(false) }
    val expandedForms = remember { mutableStateOf(false) }
    val expandedMedia = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navbar(navController, sidebarOpen)

        Text(
            text = "Web Scraper",
            fontFamily = DidactGothic,
            fontSize = 28.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            label = { Text("Enter Web Address", fontFamily = Roboto) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Button(
            onClick = {
                if (urlInput.isNotBlank()) {
                    // Placeholder scrape logic
                    scrapedTables.clear()
                    scrapedLists.clear()
                    scrapedForms.clear()
                    scrapedMedia.clear()
                    scrapedTables.addAll(listOf("Table 1", "Table 2"))
                    scrapedLists.addAll(listOf("List A", "List B"))
                    scrapedForms.addAll(listOf("Form X", "Form Y"))
                    scrapedMedia.addAll(listOf("Image1.png", "VideoClip.mp4"))
                    Toast.makeText(context, "Scraping complete!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please enter a valid URL", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PaleBlue,
                contentColor = DarkGray
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Fetch Data",
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { DropdownSection("Tables", scrapedTables, expandedTables) }
            item { DropdownSection("Lists", scrapedLists, expandedLists) }
            item { DropdownSection("Forms", scrapedForms, expandedForms) }
            item { DropdownSection("Media", scrapedMedia, expandedMedia) }
        }
    }
}

@Composable
fun DropdownSection(title: String, items: List<String>, expanded: MutableState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(14.dp))
            .background(Color.DarkGray, shape = RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .clickable { expanded.value = !expanded.value }
        ) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = DidactGothic,
                fontSize = 20.sp
            )
            Text(
                text = if (expanded.value) "▲" else "▼",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        if (expanded.value) {
            items.forEach { item ->
                Text(
                    text = item,
                    color = Color.White,
                    fontFamily = Roboto,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }
        }
    }
}
