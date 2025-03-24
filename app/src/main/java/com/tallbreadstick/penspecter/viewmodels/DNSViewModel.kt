package com.tallbreadstick.penspecter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tallbreadstick.penspecter.models.DomainInfoResponse
import com.tallbreadstick.penspecter.tools.api.RetrofitClient
import com.tallbreadstick.penspecter.tools.api.getShodanApiKey
import kotlinx.coroutines.launch

class DNSViewModel : ViewModel() {

    fun fetchResolvedDns(hostnames: String, onResult: (List<Pair<String, String?>>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.resolveDns(hostnames, getShodanApiKey())

                val resultList = response.entries.map { (key, value) -> key to value }

                onResult(resultList)
            } catch (e: Exception) {
                onResult(listOf("Error" to e.message))
            }
        }
    }

    fun fetchReversedDns(ips: String, onResult: (List<Pair<String, String?>>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.reverseDns(ips, getShodanApiKey())

                val resultList = response?.entries
                    ?.flatMap { (key, values) -> values.map { key to it } }
                    ?: listOf("Reverse Lookup" to "No results found for IPs: $ips")

                onResult(resultList)
            } catch (e: Exception) {
                onResult(listOf("Error" to e.message))
            }
        }
    }

    fun fetchDomainInfo(domain: String, onResult: (DomainInfoResponse?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getDomainInfo(domain, getShodanApiKey())
                onResult(response)
            } catch (e: Exception) {
                onResult(null) // Handle errors gracefully
            }
        }
    }


}
