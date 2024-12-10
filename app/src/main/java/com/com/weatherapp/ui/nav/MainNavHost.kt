package com.com.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.com.weatherapp.ui.HomePage
import com.com.weatherapp.ui.ListPage
import com.com.weatherapp.ui.MapPage
import com.com.weatherapp.ui.viewmodels.MainViewModel
import com.com.weatherapp.ui.nav.Route

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> {
            // Passando o viewModel para a HomePage
            HomePage(viewModel = viewModel)
        }
        composable<Route.List> {
            // Passando o viewModel para a ListPage
            ListPage(viewModel = viewModel)
        }
        composable<Route.Map> {
            // Passando o viewModel para a MapPage
            MapPage(viewModel = viewModel)
        }
    }
}
