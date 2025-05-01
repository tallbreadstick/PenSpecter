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
import org.jsoup.nodes.Element
import com.tallbreadstick.penspecter.tools.ScraperTools
import com.tallbreadstick.penspecter.tools.EndpointData

class ScraperViewModel : ViewModel() {
    var scrapedEndpoints = mutableStateListOf<EndpointData>()

    fun scrapeWebsite(url: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Fetch HTML content
                val doc = fetchHtml(url)

                // Scrape for endpoints from HTML and JavaScript files
                val endpoints = ScraperTools.scrapeEndpointsFromHtmlAndJS(doc)

                scrapedEndpoints.clear()
                scrapedEndpoints.addAll(endpoints)

                // Call onSuccess to notify the UI
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

    // Function to extract inline JS and script src URLs
    fun scrapeEndpointsFromHtmlAndJS(doc: Document): List<EndpointData> {
        val endpoints = mutableListOf<EndpointData>()

        // Scrape for JavaScript files in script tags (external .js files)
        val scriptTags = doc.select("script[src]")
        scriptTags.forEach { script ->
            val src = script.attr("src")
            // Add endpoint data for the JavaScript file source
            endpoints.add(EndpointData(url = src, method = "GET", headers = emptyMap(), bodyFormat = "None"))
        }

        // Scrape inline JavaScript in script tags
        val inlineScripts = doc.select("script:not([src])")
        inlineScripts.forEach { script ->
            val scriptContent = script.data()
            // You can parse or search for API endpoints within this script content if needed
            // For example, check for fetch() or XMLHttpRequest calls and extract endpoints
            // For now, we just add the inline script as a potential endpoint (you could expand this)
            endpoints.add(EndpointData(url = "Inline JavaScript", method = "POST", headers = emptyMap(), bodyFormat = scriptContent))
        }

        // Optionally, you can also check for other embedded JavaScript code patterns
        return endpoints
    }
}
