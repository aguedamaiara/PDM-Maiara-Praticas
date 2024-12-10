package com.com.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.com.weatherapp.ui.CityDialog
import com.com.weatherapp.ui.nav.BottomNavBar
import com.com.weatherapp.ui.nav.BottomNavItem
import com.com.weatherapp.ui.nav.MainNavHost
import com.com.weatherapp.ui.theme.WeatherAppTheme
import com.com.weatherapp.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            // Variável local para controlar o diálogo
            var showDialog by remember { mutableStateOf(false) }

            WeatherAppTheme {
                if (showDialog) {
                    CityDialog(
                        onDismiss = { showDialog = false }, // Fecha o diálogo
                        onConfirm = { city ->
                            if (city.isNotBlank()) {
                                viewModel.add(city) // Adiciona a cidade ao ViewModel
                                showDialog = false
                            }
                        }
                    )
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Bem-vindo/a!") },
                            actions = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ExitToApp,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        // Navegação inferior
                        val items = listOf(
                            BottomNavItem.HomeButton,
                            BottomNavItem.ListButton,
                            BottomNavItem.MapButton,
                        )
                        BottomNavBar(navController = navController, items)
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            showDialog = true
                        }) { // Define showDialog como true
                            Icon(Icons.Default.Add, contentDescription = "Adicionar cidade")
                        }
                    }
                ) { innerPadding ->
                    // Passando o viewModel para o MainNavHost
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainNavHost(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
