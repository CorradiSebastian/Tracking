package com.sebastiancorradi.track.navigation

sealed class AppScreens (val route:String){
    object SplashScreen: AppScreens("splash_screen")
    object MainScreen: AppScreens("main_screen")
    object LocationScreen: AppScreens("location_screen")
}