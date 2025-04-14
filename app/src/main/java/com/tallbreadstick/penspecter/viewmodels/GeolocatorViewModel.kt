package com.tallbreadstick.penspecter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tallbreadstick.penspecter.tools.api.RetrofitClient
import com.tallbreadstick.penspecter.tools.api.getLocationApiKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeolocatorViewModel : ViewModel() {
    private val _latitude = MutableStateFlow(37.7749)  // Default San Francisco
    val latitude: StateFlow<Double> = _latitude

    private val _longitude = MutableStateFlow(-122.4194)
    val longitude: StateFlow<Double> = _longitude

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchIP(ip: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val apiKey = getLocationApiKey()
                val response = RetrofitClient.locationService.getLocation(apiKey, ip)
                _latitude.value = response.latitude
                _longitude.value = response.longitude
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }
}
