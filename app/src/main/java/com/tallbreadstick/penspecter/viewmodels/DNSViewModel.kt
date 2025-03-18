package com.tallbreadstick.penspecter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tallbreadstick.penspecter.tools.api.RetrofitClient
import com.tallbreadstick.penspecter.tools.api.getApiKey
import kotlinx.coroutines.launch

class DNSViewModel : ViewModel() {
    fun fetchDns(hostnames: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.resolveDns(hostnames, getApiKey())
                val result = response.entries.joinToString("\n") { "${it.key}: ${it.value}" }
                onResult(result)
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }
}
