package com.sebastiancorradi.track.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.ui.location.LocationScreen
import com.sebastiancorradi.track.ui.location.LocationViewModel
import com.sebastiancorradi.track.ui.main.MainScreen
import com.sebastiancorradi.track.ui.splash.SplashScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route,
        ){
        composable(AppScreens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(AppScreens.MainScreen.route){
            MainScreen(navController)
        }
        composable(AppScreens.LocationScreen.route){
            LocationScreen()
        }
    }
}