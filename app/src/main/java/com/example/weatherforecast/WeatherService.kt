package com.example.weatherforecast

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class WeatherService(private val apiKey: String) {

    suspend fun fetchWeather(city: String): WeatherData? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$apiKey"
                val response = URL(url).readText(Charsets.UTF_8)
                parseWeather(response)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun parseWeather(jsonResponse: String): WeatherData {
        val jsonObj = JSONObject(jsonResponse)
        val main = jsonObj.getJSONObject("main")
        val wind = jsonObj.getJSONObject("wind")
        val weatherJson = jsonObj.getJSONArray("weather").getJSONObject(0)

        val temperature = "${main.getString("temp")}° C"
        val humidity = "${main.getString("humidity")}%"
        val windSpeed = "${wind.getString("speed")} km/h"
        val condition = weatherJson.getString("main")
        val icon = weatherJson.getString("icon")

        return WeatherData(temperature, humidity, windSpeed, condition, icon)
    }
}