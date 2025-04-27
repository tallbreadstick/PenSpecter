package com.tallbreadstick.penspecter.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ScraperViewModel : ViewModel() {
    var scrapedTables = mutableStateListOf<String>()
    var scrapedLists = mutableStateListOf<String>()
    var scrapedForms = mutableStateListOf<String>()
    var scrapedMedia = mutableStateListOf<String>()

    fun scrapeWebsite(url: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Make a network request on the IO thread
                val doc = fetchHtml(url)

                // Parse the HTML content for relevant elements
                parseHtml(doc)

                // Call onSuccess to update the UI
                onSuccess()
            } catch (e: Exception) {
                // If there is an error, notify the UI
                onError("Failed to scrape the website: ${e.message}")
            }
        }
    }

    private suspend fun fetchHtml(url: String): Document {
        return withContext(Dispatchers.IO) {
            val response = OkHttpClient().newCall(Request.Builder().url(url).build()).execute()
            val html = response.body?.string() ?: ""
            Jsoup.parse(html)
        }
    }

    private fun parseHtml(doc: Document) {
        // Scrape Tables
        scrapedTables.clear()
        doc.select("table").forEach { table ->
            scrapedTables.add(table.toString())  // Extract table data
        }

        // Scrape Lists
        scrapedLists.clear()
        doc.select("ul, ol").forEach { list ->
            scrapedLists.add(list.toString())  // Extract list data
        }

        // Scrape Forms
        scrapedForms.clear()
        doc.select("form").forEach { form ->
            scrapedForms.add(form.toString())  // Extract form data
        }

        // Scrape Media
        scrapedMedia.clear()
        doc.select("img, video, audio").forEach { media ->
            scrapedMedia.add(media.toString())  // Extract media data
        }
    }
}
