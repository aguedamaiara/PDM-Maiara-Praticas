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
import com.com.weatherapp.model.Forecast
import com.com.weatherapp.model.Weather
import com.com.weatherapp.ui.nav.Route

class MainViewModel (private val db: FBDatabase,
                     private val service : WeatherService): ViewModel(), FBDatabase.Listener {

    private val _cities = mutableStateMapOf<String, City>()
    val cities : List<City>
        get() = _cities.values.toList()


    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    // Propriedade para a cidade selecionada
    private var _city = mutableStateOf<City?>(null)
    var city: City?
        get() = _city.value
        set(tmp) { _city = mutableStateOf(tmp?.copy()) }

    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

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

    fun loadForecast(city : City) {
        service.getForecast(city.name) { result ->
            city.forecast = result?.forecast?.forecastday?.map {
                Forecast(
                    date = it.date?:"00-00-0000",
                    weather = it.day?.condition?.text?:"Erro carregando!",
                    tempMin = it.day?.mintemp_c?:-1.0,
                    tempMax = it.day?.maxtemp_c?:-1.0,
                    imgUrl = ("https:" + it.day?.condition?.icon)
                )
            }
            _cities.remove(city.name)
            _cities[city.name] = city.copy()
        }
    }

    fun loadBitmap(city: City) {
        service.getBitmap(city.weather!!.imgUrl) { bitmap ->
            city.weather!!.bitmap = bitmap
            onCityUpdate(city)
        }
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onCityAdded(city: City) {
        _cities[city.name] = city
    }

    override fun onCityUpdate(city: City) {
        _cities.remove(city.name)
        _cities[city.name] = city.copy()
        if (_city.value?.name == city.name) {
            _city.value = city.copy()
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
