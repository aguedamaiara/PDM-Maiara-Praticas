package com.com.weatherapp.api

data class APICurrentWeather (
    var location : APILocation? = null,
    var current : APIWeather? = null
)
