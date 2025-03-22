package com.tallbreadstick.penspecter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tallbreadstick.penspecter.tools.api.RetrofitClient
import com.tallbreadstick.penspecter.tools.api.getApiKey
import kotlinx.coroutines.launch

class DNSViewModel : ViewModel() {

    fun fetchResolvedDns(hostnames: String, onResult: (String) -> Unit) {
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

    fun fetchReversedDns(ips: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.reverseDns(ips, getApiKey())

                val result = response?.entries
                    ?.takeIf { it.isNotEmpty() }
                    ?.joinToString("\n") { "${it.key}: ${it.value.joinToString(", ")}" }
                    ?: "No results found for IPs: $ips"

                onResult(result)
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }

}
