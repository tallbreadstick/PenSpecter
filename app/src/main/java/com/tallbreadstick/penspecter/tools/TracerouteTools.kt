package com.tallbreadstick.penspecter.tools

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun runTraceroute(
    host: String,
    updateResults: (String) -> Unit,
    isLoading: MutableState<Boolean>
) {
    try {
        for (ttl in 1..60) {
            val command = "ping -c 1 -t $ttl $host"
            val process = ProcessBuilder()
                .command("sh", "-c", command)
                .redirectErrorStream(true)
                .start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var hopResult = ""

            reader.useLines { lines ->
                lines.forEach { line ->
                    val parsedResult = parsePingOutput(line, ttl)
                    if (parsedResult.isNotEmpty()) {
                        hopResult = parsedResult
                        updateResults(hopResult)
                    }
                }
            }

            // process.waitFor()

            if (hopResult.contains(host)) break
        }
    } catch (e: Exception) {
        updateResults("Error: ${e.message}")
    } finally {
        isLoading.value = false
    }
}


fun parsePingOutput(output: String, ttl: Int): String {
    return when {
        output.contains("From") -> output.substringAfter("From").substringBefore(":").trim()
        output.contains("time=") -> output.substringAfter("time=").substringBefore(" ms").trim()
        else -> "* * * Request timed out (TTL: $ttl)"
    }
}
