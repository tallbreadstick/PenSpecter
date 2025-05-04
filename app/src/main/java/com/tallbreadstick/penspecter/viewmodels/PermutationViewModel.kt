package com.tallbreadstick.penspecter.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.tallbreadstick.penspecter.tools.api.RetrofitClient
import java.io.IOException

class PermutationViewModel : ViewModel() {

    val selectedMethod = MutableStateFlow("GET")
    val endpointUrl = MutableStateFlow("")
    val headers = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val bodyContent = MutableStateFlow("")
    val selectedCharset = MutableStateFlow("alphabetic")
    val maxLength = MutableStateFlow("")

    private var attackJob: Job? = null
    private val _currentPassword = MutableStateFlow<String?>(null)
    val currentPassword: StateFlow<String?> = _currentPassword

    private val _matchFound = MutableStateFlow<String?>(null)
    val matchFound: StateFlow<String?> = _matchFound

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished

    fun validateInputs(
        endpoint: String,
        body: String,
        headers: List<Pair<String, String>>,
        maxLen: String
    ): String? {
        if (endpoint.isBlank()) return "Endpoint URL is required"
        if (!headers.any { it.first.contains("\$PASSWORD", true) || it.second.contains("\$PASSWORD", true) }
            && !body.contains("\$PASSWORD", true)
            && !endpoint.contains("\$PASSWORD", true)
        ) {
            return "At least one field must include the \$PASSWORD token"
        }
        if (maxLen.isBlank()) return "Max length is required"
        if (maxLen.toIntOrNull() == null || maxLen.toInt() < 1) return "Max length must be a valid number ≥ 1"
        return null
    }

    fun launchPermutationAttack(
        onMatchFound: (String) -> Unit,
        onFinished: () -> Unit
    ) {
        val charset = when (selectedCharset.value) {
            "alphabetic" -> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            "numeric" -> "0123456789"
            "alphanumeric" -> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            "ascii" -> (33..126).map { it.toChar() }.joinToString("")
            else -> ""
        }

        val maxLen = maxLength.value.toIntOrNull() ?: return
        val urlTemplate = endpointUrl.value
        val bodyTemplate = bodyContent.value
        val headerTemplate = headers.value

        _matchFound.value = null
        _isFinished.value = false

        attackJob = viewModelScope.launch(Dispatchers.IO) {
            generatePermutations(charset, maxLen).collect { password ->
                _currentPassword.value = password
                val finalUrl = urlTemplate.replace("\$PASSWORD", password)
                val finalBody = bodyTemplate.replace("\$PASSWORD", password)
                val finalHeaders = headerTemplate.map {
                    it.first.replace("\$PASSWORD", password) to it.second.replace("\$PASSWORD", password)
                }

                val requestBuilder = Request.Builder().url(finalUrl)
                for ((key, value) in finalHeaders) {
                    if (key.isNotBlank()) requestBuilder.addHeader(key, value)
                }

                val client = RetrofitClient.sharedClient // Using the shared client
                val mediaType = "application/json".toMediaType()
                val body = finalBody.toRequestBody(mediaType)

                when (selectedMethod.value.uppercase()) {
                    "POST" -> requestBuilder.post(body)
                    "PUT" -> requestBuilder.put(body)
                    "DELETE" -> requestBuilder.delete()
                    else -> requestBuilder.get()
                }

                val request = requestBuilder.build()
                try {
                    val response = client.newCall(request).execute()
                    val bodyString = response.body?.string() ?: ""

                    if (bodyString.contains("Success", true)) {
                        _matchFound.value = password
                        onMatchFound(password)
                        cancelAttack()
                        return@collect
                    }

                } catch (e: IOException) {
                    Log.e("PERM_ATTACK", "Request failed for $password: ${e.message}")
                }
            }

            _isFinished.value = true
            onFinished()
        }
    }

    fun cancelAttack() {
        attackJob?.cancel()
        _isFinished.value = true
    }

    private fun generatePermutations(chars: String, maxLen: Int) = flow {
        suspend fun backtrack(current: StringBuilder, depth: Int) {
            if (!currentCoroutineContext().isActive) return
            if (depth > 0) emit(current.toString())
            if (depth == maxLen) return

            for (ch in chars) {
                current.append(ch)
                backtrack(current, depth + 1)
                current.deleteCharAt(current.lastIndex)
            }
        }

        backtrack(StringBuilder(), 0)
    }.flowOn(Dispatchers.Default)
}
