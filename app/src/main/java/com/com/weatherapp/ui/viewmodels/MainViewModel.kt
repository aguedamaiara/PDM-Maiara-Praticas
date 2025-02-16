package com.com.weatherapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.com.weatherapp.model.City
import com.com.weatherapp.model.User
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.com.weatherapp.db.fb.FBDatabase
import com.com.weatherapp.api.WeatherService
import com.com.weatherapp.model.Weather

class MainViewModel (private val db: FBDatabase,
                     private val service : WeatherService): ViewModel(), FBDatabase.Listener {

    private val _cities = mutableStateMapOf<String, City>()
    val cities: List<City>
        get() = _cities.values.toList()


    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    // Função para remover uma cidade da lista
    fun remove(city: City) {
        db.remove(city)
    }

    // Função para adicionar uma cidade à lista
    fun add(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)))
            }
        }
    }
    fun add(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location))
            }
        }
    }

    fun loadWeather(city: City) {
        service.getCurrentWeather(city.name) { apiWeather ->
            city.weather = Weather (
                date = apiWeather?.current?.last_updated?:"...",
                desc = apiWeather?.current?.condition?.text?:"...",
                temp = apiWeather?.current?.temp_c?:-1.0,
                imgUrl = "https:" + apiWeather?.current?.condition?.icon
            )
            _cities.remove(city.name)
            _cities[city.name] = city.copy()
        }
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onCityAdded(city: City) {
        _cities[city.name] = city
    }

    override fun onCityUpdate(city: City) {
        if (_cities.containsKey(city.name)) {
            _cities[city.name] = city.copy()
        }
    }

    override fun onCityRemoved(city: City) {
        _cities.remove(city.name)
    }
}


/*private fun generateCities(): List<City> {
    return List(20) { i ->
        City(name = "Cidade $i", weather = "Carregando clima...")
    }
}*/
