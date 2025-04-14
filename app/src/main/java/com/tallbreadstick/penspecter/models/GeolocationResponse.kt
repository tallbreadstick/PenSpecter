package com.tallbreadstick.penspecter.models

data class GeoLocationResponse(
    val ip: String,
    val latitude: Double,
    val longitude: Double,
    val city: String?,
    val countryName: String?
)
