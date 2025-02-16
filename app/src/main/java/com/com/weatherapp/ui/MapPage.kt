package com.com.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.com.weatherapp.R
import com.com.weatherapp.ui.viewmodels.MainViewModel

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPage(viewModel: MainViewModel){

    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val recife = LatLng(-8.05, -34.9)
    val caruaru = LatLng(-8.27, -35.98)
    val joaopessoa = LatLng(-7.12, -34.84)
    val camPosState = rememberCameraPositionState ()

    GoogleMap (
        modifier = Modifier.fillMaxSize(),
        onMapClick = { location ->
            viewModel.add(location = location) // Adicionando apenas a localização
        },
        cameraPositionState = camPosState,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    ) { listOf(
        Triple(recife, "Recife", BitmapDescriptorFactory.HUE_BLUE),
        Triple(caruaru, "Caruaru", BitmapDescriptorFactory.HUE_GREEN),
        Triple(joaopessoa, "João Pessoa", BitmapDescriptorFactory.HUE_RED)
    ).forEach { (position, title, color) ->
        Marker(
            state = MarkerState(position = position),
            title = title,
            snippet = "Marcador em $title",
            icon = BitmapDescriptorFactory.defaultMarker(color)
        )
    }

        viewModel.cities.forEach { city ->
            city.location?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = city.name,
                    snippet = "${location.latitude}, ${location.longitude}"
                )
            }
        }
    }
}

