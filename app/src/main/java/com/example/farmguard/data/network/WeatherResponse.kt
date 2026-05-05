package com.example.farmguard.data.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current") val current: Current
)

data class Current(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("weather_code") val weatherCode: Int
)

data class GeocodingResponse(
    @SerializedName("results") val results: List<GeocodingResult>?
)

data class GeocodingResult(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String?
)
