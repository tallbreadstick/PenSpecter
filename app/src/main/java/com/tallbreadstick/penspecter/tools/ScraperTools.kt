package com.tallbreadstick.penspecter.tools

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object ScraperTools {

    // Function to scrape all endpoints (including their details like method, headers, body)
    fun scrapeEndpointsFromHtmlAndJS(doc: Document): List<EndpointData> {
        val endpoints = mutableListOf<EndpointData>()

        // Scrape external script files (src attribute of script tags)
        val scriptElements: Elements = doc.select("script[src]")
        scriptElements.forEach { script ->
            val scriptSrc = script.attr("src")
            // Add endpoint data for external JavaScript file sources
            endpoints.add(EndpointData(url = scriptSrc, method = "GET", headers = emptyMap(), bodyFormat = "None"))
        }

        // Scrape inline JavaScript (without src attribute)
        val inlineScriptElements: Elements = doc.select("script:not([src])")
        inlineScriptElements.forEach { script ->
            val scriptContent = script.data()

            // Check for 'fetch' requests
            val fetchPattern = Regex("""fetch\((.*?)\)""")
            val fetchMatches = fetchPattern.findAll(scriptContent)

            fetchMatches.forEach { match ->
                val endpointUrl = match.groupValues[1]
                val headers = extractHeadersFromRequest(match.value)
                val method = "GET" // For simplicity, assuming GET. You can extract it based on the script if needed.
                val bodyFormat = "JSON" // If there's a body, this can be extracted as well.
                endpoints.add(EndpointData(endpointUrl, headers, method, bodyFormat))
            }

            // Check for 'axios' requests
            val axiosPattern = Regex("""axios\((.*?)\)""")
            val axiosMatches = axiosPattern.findAll(scriptContent)

            axiosMatches.forEach { match ->
                val endpointUrl = match.groupValues[1]
                val headers = extractHeadersFromRequest(match.value)
                val method = "GET" // Same logic as for fetch
                val bodyFormat = "JSON"
                endpoints.add(EndpointData(endpointUrl, headers, method, bodyFormat))
            }

            // Check for 'XMLHttpRequest' requests
            val xhrPattern = Regex("""new\sXMLHttpRequest\(\)\.open\((.*?)\)""")
            val xhrMatches = xhrPattern.findAll(scriptContent)

            xhrMatches.forEach { match ->
                val endpointUrl = match.groupValues[1]
                val headers = extractHeadersFromRequest(match.value)
                val method = "GET"
                val bodyFormat = "JSON"
                endpoints.add(EndpointData(endpointUrl, headers, method, bodyFormat))
            }
        }

        return endpoints
    }

    // Helper function to extract headers from the request
    private fun extractHeadersFromRequest(request: String): Map<String, String> {
        // Look for headers in the format { 'Content-Type': 'application/json' }
        val headersPattern = Regex("""\{(.*?)\}""")
        val headersMatch = headersPattern.find(request)
        val headers = mutableMapOf<String, String>()

        headersMatch?.groupValues?.get(1)?.split(",")?.forEach { header ->
            val headerParts = header.split(":")
            if (headerParts.size == 2) {
                val key = headerParts[0].trim().removeSurrounding("'", "\"")
                val value = headerParts[1].trim().removeSurrounding("'", "\"")
                headers[key] = value
            }
        }

        return headers
    }
}

// Data class for endpoint data
data class EndpointData(
    val url: String,
    val headers: Map<String, String>,
    val method: String,
    val bodyFormat: String
)
