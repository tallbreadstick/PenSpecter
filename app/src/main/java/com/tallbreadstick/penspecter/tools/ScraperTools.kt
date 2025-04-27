package com.tallbreadstick.penspecter.tools

import org.jsoup.Jsoup

object ScraperTools {

    fun parseTable(html: String): List<List<String>> {
        val table = Jsoup.parse(html).selectFirst("table") ?: return emptyList()
        return table.select("tr").map { row ->
            row.select("th, td").map { it.text() }
        }
    }

    fun parseList(html: String): List<String> {
        val list = Jsoup.parse(html).selectFirst("ul, ol") ?: return emptyList()
        return list.select("li").map { it.text() }
    }

    fun parseForm(html: String): List<Pair<String, String>> {
        val form = Jsoup.parse(html).selectFirst("form") ?: return emptyList()
        return form.select("input, select, textarea").map { input ->
            val label = input.attr("name").ifEmpty { input.tagName() }
            val type = input.attr("type").ifEmpty { "text" }
            label to type
        }
    }

    fun parseMedia(html: String, baseUrl: String): List<String> {
        val doc = Jsoup.parse(html)
        val mediaUrls = mutableListOf<String>()

        // Prepend baseUrl to relative URLs
        doc.select("img, video, audio").forEach { media ->
            var src = media.attr("src")
            if (src.startsWith("data:") || src.startsWith("http") || src.startsWith("https")) {
                // Already absolute, no change needed
                mediaUrls.add(src)
            } else {
                // Handle relative URLs
                if (src.startsWith("/")) {
                    // Prepend base URL if relative URL starts with '/'
                    src = "$baseUrl$src"
                } else {
                    // Prepend base URL to relative path
                    src = "$baseUrl/$src"
                }
                mediaUrls.add(src)
            }
        }
        return mediaUrls
    }
}
