package com.com.weatherapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.com.weatherapp.R

@Composable
fun ListPage(modifier: Modifier = Modifier) {
    val cityList = remember { getCities().toMutableStateList() }
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(cityList) { city ->
            CityItem(
                city = city,
                onClose = {
                    // Remove a cidade da lista quando clicar no botão de fechar
                    cityList.remove(city)
                    Toast.makeText(context, "Cidade removida: ${city.name}", Toast.LENGTH_SHORT).show()
                },
                onClick = {
                    Toast.makeText(context, "Cidade selecionada: ${city.name}", Toast.LENGTH_SHORT).show()

                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListPagePreview() {
    ListPage()
}

data class City(
    val name: String,
    val weather: String? = null,
    val location: String? = null
)

private fun getCities() = List(20) { i ->
    City(name = "Cidade $i", weather = "Carregando clima...")
}

@Composable
fun CityItem(
    city: City,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.FavoriteBorder,
            contentDescription = "Favorite Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = city.name,
                fontSize = 24.sp
            )
            Text(
                text = city.weather ?: "Carregando clima...",
                fontSize = 16.sp
            )
        }
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close"
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CityItemPreview() {
    CityItem(
        city = City(name = "Recife", weather = "Ensolarado"),
        onClick = {},
        onClose = {}
    )
}

