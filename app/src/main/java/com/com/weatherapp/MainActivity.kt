package com.com.weatherapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.com.weatherapp.ui.CityDialog
import com.com.weatherapp.ui.nav.BottomNavBar
import com.com.weatherapp.ui.nav.BottomNavItem
import com.com.weatherapp.ui.nav.MainNavHost
import com.com.weatherapp.ui.nav.Route
import com.com.weatherapp.ui.theme.WeatherAppTheme
import com.com.weatherapp.ui.viewmodels.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.com.weatherapp.db.fb.FBDatabase
import com.com.weatherapp.ui.viewmodels.MainViewModelFactory
import com.com.weatherapp.api.WeatherService

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
            val currentRoute = navController.currentBackStackEntryAsState()
            val showButton = currentRoute.value?.destination?.hasRoute(Route.List::class)?:false
            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {} )

            val fbDB = remember { FBDatabase() }
            val weatherService = remember { WeatherService() }
            val viewModel : MainViewModel = viewModel(
                factory = MainViewModelFactory(fbDB, weatherService)
            )

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
                            title = {
                                val name = viewModel.user?.name?:"[não logado]"
                                Text("Bem-vindo/a! $name")
                            },
                            actions = {
                                IconButton( onClick = {
                                    Firebase.auth.signOut()
                                    finish()
                                } ) {
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
                        BottomNavBar(viewModel, items)
                    },
                    floatingActionButton = {
                        if (showButton) {
                            FloatingActionButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) { innerPadding ->
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainNavHost(navController = navController,
                            viewModel = viewModel)
                    }
                    LaunchedEffect(viewModel.page) {
                        navController.navigate(viewModel.page) {
                            // Volta pilha de navegação até HomePage (startDest).
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}