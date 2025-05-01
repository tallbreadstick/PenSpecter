import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun runPing(host: String): String = withContext(Dispatchers.IO) {
    try {
        val process = ProcessBuilder()
            .command("sh", "-c", "ping -c 1 $host")
            .redirectErrorStream(true)
            .start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()

        if (output.contains("time=")) {
            val time = Regex("time=([0-9.]+) ms")
                .find(output)
                ?.groupValues?.get(1)
            "${time}ms"
        } else {
            "Ping failed or host unreachable."
        }
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}
