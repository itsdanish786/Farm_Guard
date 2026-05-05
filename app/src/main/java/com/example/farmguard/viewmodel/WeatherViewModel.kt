package com.example.farmguard.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmguard.data.network.WeatherApiService
import com.example.farmguard.data.network.WeatherResponse
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    var weatherResponse by mutableStateOf<WeatherResponse?>(null)
    var cityName by mutableStateOf("Current Location")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApi = retrofit.create(WeatherApiService::class.java)

    @SuppressLint("MissingPermission")
    fun fetchWeatherByLocation() {
        Log.d("WeatherViewModel", "fetchWeatherByLocation called")
        isLoading = true
        errorMessage = null
        
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { location ->
                Log.d("WeatherViewModel", "Location success: $location")
                if (location != null) {
                    fetchWeather(location.latitude, location.longitude, "Current Location")
                } else {
                    Log.d("WeatherViewModel", "Location is null, falling back to lastLocation")
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                        if (lastLoc != null) {
                            fetchWeather(lastLoc.latitude, lastLoc.longitude, "Current Location")
                        } else {
                            Log.d("WeatherViewModel", "lastLocation is also null, falling back to London")
                            fetchWeatherByCity("London")
                        }
                    }.addOnFailureListener {
                        fetchWeatherByCity("London")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("WeatherViewModel", "Location failure", e)
                fetchWeatherByCity("London")
            }
    }

    private fun fetchWeather(lat: Double, lon: Double, name: String) {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Fetching weather for $lat, $lon")
                val response = weatherApi.getCurrentWeather(lat, lon)
                weatherResponse = response
                cityName = name
                Log.d("WeatherViewModel", "Weather fetch success: ${response.current.temperature}")
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Weather fetch error", e)
                errorMessage = "Weather Error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchWeatherByCity(city: String) {
        Log.d("WeatherViewModel", "fetchWeatherByCity called for $city")
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val geo = weatherApi.searchCity(city)
                val result = geo.results?.firstOrNull()
                if (result != null) {
                    Log.d("WeatherViewModel", "City found: ${result.latitude}, ${result.longitude}")
                    val response = weatherApi.getCurrentWeather(result.latitude, result.longitude)
                    weatherResponse = response
                    cityName = result.name
                } else {
                    Log.d("WeatherViewModel", "City results empty")
                    errorMessage = "City not found"
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "City search error", e)
                errorMessage = "Search Error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Mainly clear"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            61, 63, 65 -> "Rain"
            71, 73, 75 -> "Snow"
            80, 81, 82 -> "Rain showers"
            95, 96, 99 -> "Thunderstorm"
            else -> "Cloudy"
        }
    }
}
