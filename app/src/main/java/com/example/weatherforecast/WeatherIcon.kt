package com.example.weatherforecast

enum class WeatherIcon(val code: String, val drawableRes: Int) {
    CLEAR_DAY("01d", R.drawable.sun),
    CLEAR_NIGHT("01n", R.drawable.moon),
    FEW_CLOUDS_DAY("02d", R.drawable.suncloud),
    FEW_CLOUDS_NIGHT("02n", R.drawable.mooncloud),
    CLOUDS_DAY("03d", R.drawable.cloudy),
    CLOUDS_NIGHT("03n", R.drawable.cloudy),
    BROKEN_CLOUDS_DAY("04d", R.drawable.cloudy),
    BROKEN_CLOUDS_NIGHT("04n", R.drawable.cloudy),
    SHOWER_RAIN_DAY("09d", R.drawable.rain),
    SHOWER_RAIN_NIGHT("09n", R.drawable.rain),
    RAIN_DAY("10d", R.drawable.rain),
    RAIN_NIGHT("10n", R.drawable.rain),
    THUNDERSTORM_DAY("11d", R.drawable.storm),
    THUNDERSTORM_NIGHT("11n", R.drawable.storm),
    SNOW_DAY("13d", R.drawable.snow),
    SNOW_NIGHT("13n", R.drawable.snow),
    MIST_DAY("50d", R.drawable.mist),
    MIST_NIGHT("50n", R.drawable.mist);

    companion object {
        fun fromCode(code: String): WeatherIcon =
            values().find { it.code == code } ?: CLEAR_DAY
    }
}