package com.example.weatherforecast

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class WeatherData(
    val temperature: String,
    val humidity: String,
    val windSpeed: String,
    val condition: String,
    val icon: String
)

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

class MainActivity : AppCompatActivity() {

    private lateinit var holder: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var cityText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windText: TextView
    private lateinit var conditionText: TextView
    private lateinit var conditionImage: ImageView
    private lateinit var cityEdit: EditText
    private lateinit var changeCityButton: Button

    private val weatherIconMap: Map<String, Int> = mapOf(
        "01d" to R.drawable.sun,
        "01n" to R.drawable.moon,
        "02d" to R.drawable.suncloud,
        "02n" to R.drawable.mooncloud,
        "03d" to R.drawable.cloudy,
        "03n" to R.drawable.cloudy,
        "04d" to R.drawable.cloudy,
        "04n" to R.drawable.cloudy,
        "09d" to R.drawable.rain,
        "09n" to R.drawable.rain,
        "10d" to R.drawable.rain,
        "10n" to R.drawable.rain,
        "11d" to R.drawable.storm,
        "11n" to R.drawable.storm,
        "13d" to R.drawable.snow,
        "13n" to R.drawable.snow,
        "50d" to R.drawable.mist,
        "50n" to R.drawable.mist
    )

    private var city: String = "London"
    private val apiKey: String = ""
    private lateinit var weatherService: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        holder = findViewById(R.id.holder)
        progressBar = findViewById(R.id.progressbar)
        errorText = findViewById(R.id.errorText)
        cityText = findViewById(R.id.city)
        temperatureText = findViewById(R.id.temperature)
        humidityText = findViewById(R.id.humidity)
        windText = findViewById(R.id.wind)
        conditionText = findViewById(R.id.textCondition)
        conditionImage = findViewById(R.id.imgCondition)
        cityEdit = findViewById(R.id.editTextCity)
        changeCityButton = findViewById(R.id.btnChangeCity)

        weatherService = WeatherService(apiKey)

        loadWeather()

        changeCityButton.setOnClickListener {
            val newCity = cityEdit.text.toString()
            if (newCity.isNotBlank()) {
                city = newCity
                loadWeather()
            }
        }
    }

    private fun loadWeather() {
        holder.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        errorText.visibility = View.GONE

        lifecycleScope.launch {
            val weatherData = weatherService.fetchWeather(city)
            progressBar.visibility = View.GONE
            if (weatherData != null) {
                updateWeatherUI(weatherData)
            } else {
                showError()
            }
        }
    }

    private fun updateWeatherUI(weatherData: WeatherData) {
        cityText.text = city
        temperatureText.text = weatherData.temperature
        humidityText.text = weatherData.humidity
        windText.text = weatherData.windSpeed
        conditionText.text = weatherData.condition

        val imageRes = weatherIconMap[weatherData.icon] ?: R.drawable.sun
        conditionImage.setImageResource(imageRes)

        holder.visibility = View.VISIBLE
    }

    private fun showError() {
        errorText.visibility = View.VISIBLE
        holder.visibility = View.GONE
    }
}
