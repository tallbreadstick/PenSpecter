package com.tallbreadstick.penspecter.tools

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE id = 0 LIMIT 1")
    suspend fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)

}

@Entity(tableName = "settings")
data class SettingsEntity(

    @PrimaryKey val id: Int = 0,

    var deviceDiscovery: Boolean,
    var traceroute: Boolean,
    var packetAnalyzer: Boolean,
    var dnsLookup: Boolean,
    var wifiAnalyzer: Boolean,
    var webScraper: Boolean,
    var ipGeolocator: Boolean,
    var liveFeeds: Boolean,
    var dictionaryAttack: Boolean,
    var permutationAttack: Boolean,



    )