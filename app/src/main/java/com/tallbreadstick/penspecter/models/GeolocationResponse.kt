package com.tallbreadstick.penspecter.models

import com.google.gson.annotations.SerializedName

data class GeoLocationResponse(
    val ip: String,
    val latitude: Double,
    val longitude: Double,
    val city: String?,
    @SerializedName("country_name")
    val countryName: String?
)
