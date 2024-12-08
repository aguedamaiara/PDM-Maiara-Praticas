package com.com.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.com.weatherapp.ui.HomePage
import com.com.weatherapp.ui.ListPage
import com.com.weatherapp.ui.MapPage
import com.com.weatherapp.ui.nav.Route

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> {
            HomePage()
        }
        composable<Route.List> {
            ListPage()
        }
        composable<Route.Map> {
            MapPage()
        }
    }
}