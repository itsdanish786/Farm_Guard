package com.example.farmguard.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,weather_code"
    ): WeatherResponse

    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCity(
        @Query("name") cityName: String,
        @Query("count") count: Int = 1
    ): GeocodingResponse
}
