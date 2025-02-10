package com.com.weatherapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.com.weatherapp.model.City
import com.com.weatherapp.model.User
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.mutableStateListOf
import com.com.weatherapp.db.fb.FBDatabase

class MainViewModel(private val db: FBDatabase) : ViewModel(),
    FBDatabase.Listener {
    private val _cities = mutableStateListOf<City>()

    // Acesso somente leitura à lista de cidades
    val cities
        get() = _cities.toList()

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
    fun add(name: String, location: LatLng? = null) {
        val newCity = City(name = name, location = location)
        db.add(newCity)
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onCityAdded(city: City) {
        _cities.add(city)
    }

    override fun onCityUpdate(city: City) {
        val index = _cities.indexOfFirst { it.name == city.name }
        if (index != -1) {
            _cities[index] = city
        }
    }

    override fun onCityRemoved(city: City) {
        _cities.remove(city)
    }
}

private fun generateCities(): List<City> {
    return List(20) { i ->
        City(name = "Cidade $i", weather = "Carregando clima...")
    }
}
