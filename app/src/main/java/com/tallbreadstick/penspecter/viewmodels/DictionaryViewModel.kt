package com.tallbreadstick.penspecter.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tallbreadstick.penspecter.R
import com.tallbreadstick.penspecter.tools.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.InputStreamReader

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    val endpointUrl = MutableStateFlow("")
    val selectedMethod = MutableStateFlow("GET")
    val headers = MutableStateFlow(listOf<Pair<String, String>>())
    val bodyContent = MutableStateFlow("")
    val selectedDictionary = MutableStateFlow("rockyou.txt")

    private val _attackStatus = MutableStateFlow("Idle")
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> get() = _progress

    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> get() = _total

    val attackStatus: StateFlow<String> get() = _attackStatus

    private val context = application.applicationContext
    private val client = RetrofitClient.sharedClient

    fun validateInputs(
        endpointUrl: String,
        bodyContent: String,
        headers: List<Pair<String, String>>
    ): String? {
        if (endpointUrl.isBlank()) return "Endpoint URL is required."
        if (!containsPasswordToken(endpointUrl, bodyContent, headers)) return "At least one field must include \$PASSWORD token."
        return null
    }

    private fun containsPasswordToken(
        endpointUrl: String,
        bodyContent: String,
        headers: List<Pair<String, String>>
    ): Boolean {
        if (endpointUrl.contains("\$PASSWORD")) return true
        if (bodyContent.contains("\$PASSWORD")) return true
        if (headers.any { it.first.contains("\$PASSWORD") || it.second.contains("\$PASSWORD") }) return true
        return false
    }

    fun launchDictionaryAttack(onMatchFound: (String) -> Unit, onFinished: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _attackStatus.value = "Running"
            Log.d("DICTIONARY", "Starting dictionary attack...")

            var lineCount = 0
            context.resources.openRawResource(R.raw.rockyou).bufferedReader().useLines { lines ->
                lines.forEach { lineCount++ }
            }
            _total.value = lineCount

            context.resources.openRawResource(R.raw.rockyou).bufferedReader().useLines { lines ->
                var index = 0
                lines.forEach { password ->
                    _progress.value = index + 1
                    Log.d("DICTIONARY", "Trying password at line $index: $password")

                    val request = try {
                        buildRequestWithPassword(password)
                    } catch (e: Exception) {
                        Log.e("DICTIONARY", "Failed to build request: ${e.message}")
                        index++
                        return@forEach
                    }

                    val response = try {
                        client.newCall(request).execute()
                    } catch (e: Exception) {
                        Log.e("DICTIONARY", "Request failed for $password: ${e.message}")
                        index++
                        return@forEach
                    }

                    if (response.isSuccessful) {
                        _attackStatus.value = "Match found at line $index: $password"
                        Log.d("DICTIONARY", "MATCH FOUND at line $index: $password")
                        onMatchFound(password)
                        response.close()
                        return@launch
                    }

                    response.close()
                    index++
                }

                _attackStatus.value = "No match found"
                Log.d("DICTIONARY", "Dictionary attack finished. No match found.")
                onFinished()
            }
        }
    }

    private fun buildRequestWithPassword(password: String): Request {
        val finalUrl = endpointUrl.value.trim().takeIf {
            it.startsWith("http://") || it.startsWith("https://")
        } ?: "http://${endpointUrl.value.trim()}"

        val finalUrlWithPassword = finalUrl.replace("\$PASSWORD", password)
        val finalBody = bodyContent.value.replace("\$PASSWORD", password)
        val finalHeaders = headers.value.map { it.first to it.second.replace("\$PASSWORD", password) }

        val builder = Request.Builder().url(finalUrlWithPassword)

        finalHeaders.forEach { (key, value) ->
            if (key.isNotBlank()) {
                builder.addHeader(key, value)
                Log.d("DICTIONARY", "Added header: $key: $value")
            }
        }

        val body = when {
            selectedMethod.value.equals("POST", ignoreCase = true) -> {
                if (headers.value.any { it.first.equals("Content-Type", true) && it.second.equals("application/json", true) }) {
                    finalBody.toRequestBody("application/json".toMediaTypeOrNull())
                } else {
                    finalBody.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
                }
            }
            selectedMethod.value.equals("PUT", ignoreCase = true) ||
            selectedMethod.value.equals("DELETE", ignoreCase = true) -> {
                finalBody.toRequestBody("application/json".toMediaTypeOrNull())
            }
            else -> null
        }

        if (selectedMethod.value != "GET") {
            builder.method(selectedMethod.value, body)
            Log.d("DICTIONARY", "Using method ${selectedMethod.value} with body: $finalBody")
        } else {
            builder.get()
            Log.d("DICTIONARY", "Using GET method")
        }

        return builder.build()
    }
}
