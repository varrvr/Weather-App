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
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val holder: RelativeLayout by lazy { findViewById<RelativeLayout>(R.id.holder) }
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progressbar) }
    private val errorText: TextView by lazy { findViewById<TextView>(R.id.errorText) }
    private val cityText: TextView by lazy { findViewById<TextView>(R.id.city) }
    private val temperatureText: TextView by lazy { findViewById<TextView>(R.id.temperature) }
    private val humidityText: TextView by lazy { findViewById<TextView>(R.id.humidity) }
    private val windText: TextView by lazy { findViewById<TextView>(R.id.wind) }
    private val conditionText: TextView by lazy { findViewById<TextView>(R.id.textCondition) }
    private val conditionImage: ImageView by lazy { findViewById<ImageView>(R.id.imgCondition) }
    private val cityEdit: EditText by lazy { findViewById<EditText>(R.id.editTextCity) }
    private val changeCityButton: Button by lazy { findViewById<Button>(R.id.btnChangeCity) }

    private var city: String = "London"
    private lateinit var weatherService: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        weatherService = WeatherService(BuildConfig.API_KEY)

        changeCityButton.setOnClickListener {
            val newCity = cityEdit.text.toString()
            if (newCity.isNotBlank()) {
                city = newCity
                loadWeather()
            }
        }

        loadWeather()
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

        conditionImage.setImageResource(WeatherIcon.fromCode(weatherData.icon).drawableRes)

        holder.visibility = View.VISIBLE
    }

    private fun showError() {
        errorText.visibility = View.VISIBLE
        holder.visibility = View.GONE
    }
}