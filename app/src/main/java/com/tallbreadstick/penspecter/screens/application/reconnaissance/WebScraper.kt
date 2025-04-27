package com.tallbreadstick.penspecter.screens.application.reconnaissance

import android.content.Context
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
import com.tallbreadstick.penspecter.viewmodels.ScraperViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tallbreadstick.penspecter.tools.ScraperTools

@Preview
@Composable
fun WebScraper(navController: NavController? = null, context: Context? = null) {
    val sidebarOpen = remember { mutableStateOf(false) }
    var urlInput by remember { mutableStateOf("") }

    // Get the ViewModel
    val scraperViewModel: ScraperViewModel = viewModel()

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
            label = {
                Text(
                    "Enter Web Address",
                    fontFamily = Roboto
                )
            },
            textStyle = LocalTextStyle.current.copy(
                color = Color.White
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkGray,
                unfocusedContainerColor = DarkGray,
                focusedIndicatorColor = PaleBlue,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = PaleBlue,
                focusedLabelColor = PaleBlue,
                unfocusedLabelColor = Color.LightGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Button(
            onClick = {
                if (urlInput.isNotBlank()) {
                    // Start scraping when the button is clicked
                    scraperViewModel.scrapeWebsite(urlInput,
                        onSuccess = {
                            Toast.makeText(context, "Scraping complete!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
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
            item { DropdownSection("Tables", urlInput, scraperViewModel.scrapedTables, expandedTables) }
            item { DropdownSection("Lists", urlInput, scraperViewModel.scrapedLists, expandedLists) }
            item { DropdownSection("Forms", urlInput, scraperViewModel.scrapedForms, expandedForms) }
            item { DropdownSection("Media", urlInput, scraperViewModel.scrapedMedia, expandedMedia) }
        }
    }
}

@Composable
fun DropdownSection(title: String, baseUrl: String, items: List<String>, expanded: MutableState<Boolean>) {
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
                Spacer(Modifier.height(8.dp))
                when (title) {
                    "Tables" -> {
                        val tableData = ScraperTools.parseTable(item)
                        TableComposable(tableData)
                    }
                    "Lists" -> {
                        val listItems = ScraperTools.parseList(item)
                        ListComposable(listItems)
                    }
                    "Forms" -> {
                        val formFields = ScraperTools.parseForm(item)
                        FormComposable(formFields)
                    }
                    "Media" -> {
                        val mediaUrls = ScraperTools.parseMedia(item, baseUrl)
                        MediaComposable(mediaUrls)
                    }
                    else -> {
                        Text(
                            text = item,
                            color = Color.White,
                            fontFamily = Roboto,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TableComposable(table: List<List<String>>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        table.forEach { row ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { cell ->
                    Text(
                        text = cell,
                        color = Color.White,
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f, fill = true)
                    )
                }
            }
        }
    }
}

@Composable
fun ListComposable(items: List<String>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        items.forEach { item ->
            Text(
                text = "• $item",
                color = Color.White,
                fontFamily = Roboto,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
fun FormComposable(fields: List<Pair<String, String>>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        fields.forEach { (label, type) ->
            Text(
                text = "$label ($type)",
                color = Color.White,
                fontFamily = Roboto,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
fun MediaComposable(urls: List<String>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        urls.forEach { url ->
            when {
                url.startsWith("data:image") -> {
                    // Render base64 image
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 4.dp)
                    )
                }
                url.endsWith(".jpg", true) || url.endsWith(".jpeg", true) ||
                        url.endsWith(".png", true) || url.endsWith(".gif", true) -> {
                    // Render standard image URL
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 4.dp)
                    )
                }
                else -> {
                    // Just show the media URL as text (for now)
                    Text(
                        text = "Media: $url",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
