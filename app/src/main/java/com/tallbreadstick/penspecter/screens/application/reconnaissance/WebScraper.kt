package com.tallbreadstick.penspecter.screens.application.reconnaissance

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.tallbreadstick.penspecter.tools.EndpointData

@Composable
fun WebScraper(navController: NavController? = null, scraperViewModel: ScraperViewModel, context: Context? = null) {

    var urlInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navbar(navController)

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

        // Displaying the scraped endpoints
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                scraperViewModel.scrapedEndpoints.forEach { endpoint ->
                    EndpointCard(endpoint)
                }
            }
        }
    }
}

@Composable
fun EndpointCard(endpoint: EndpointData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGray) // Dark background for card
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // URL
            Text(
                text = "Endpoint URL: ${endpoint.url}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PaleBlue, // Soft blue for URL
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // HTTP Method
            Text(
                text = "Method: ${endpoint.method}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.LightGray // Light gray for method text
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Headers
            Text(
                text = "Headers:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray // Light gray for headers label
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            endpoint.headers.forEach { (key, value) ->
                Text(
                    text = "$key: $value",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.LightGray // Light gray for header values
                    ),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            // Body Format
            Text(
                text = "Body Format: ${endpoint.bodyFormat}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.LightGray // Light gray for body format text
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
