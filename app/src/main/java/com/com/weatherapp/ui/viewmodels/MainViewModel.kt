package com.com.weatherapp.ui.viewmodels

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.com.weatherapp.model.City

class MainViewModel : ViewModel() {

    // Inicializa a lista de cidades diretamente
    private val _cities = List(20) { i ->
        City(name = "Cidade $i", weather = "Carregando clima...")
    }.toMutableStateList()

    // Acesso somente leitura à lista de cidades
    val cities
        get() = _cities.toList()

    // Função para remover uma cidade da lista
    fun remove(city: City) {
        _cities.remove(city)
    }

    // Função para adicionar uma cidade à lista
    fun add(name: String) {
        _cities.add(City(name = name))
    }
}
