package com.tallbreadstick.penspecter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.tallbreadstick.penspecter.tools.AppDatabase
import com.tallbreadstick.penspecter.tools.SettingsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val db = Room.databaseBuilder(app, AppDatabase::class.java, "app_database").build()
    private val settingsDao = db.settingsDao()

    private val _settings = MutableStateFlow(
        SettingsEntity(
            deviceDiscovery = true,
            ping = true,
            dnsLookup = true,
            wifiAnalyzer = true,
            webScraper = true,
            ipGeolocator = true,
            dictionaryAttack = true,
            permutationAttack = true
        )
    )

    val settings = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val savedSettings = settingsDao.getSettings()
            if (savedSettings != null) {
                _settings.value = savedSettings
            } else {
                settingsDao.saveSettings(_settings.value) // Save default settings if not found
            }
        }
    }

    fun updateSetting(update: (SettingsEntity) -> SettingsEntity) {
        viewModelScope.launch {
            _settings.value = update(_settings.value)
            settingsDao.saveSettings(_settings.value)
        }
    }
}