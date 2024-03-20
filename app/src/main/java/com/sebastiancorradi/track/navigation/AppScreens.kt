package com.sebastiancorradi.track.navigation

import androidx.annotation.StringRes
import com.sebastiancorradi.track.R

sealed class AppScreens (val route:String, @StringRes val resourceId: Int){
    object SplashScreen: AppScreens("splash_screen",R.string.splash)
    object MainScreen: AppScreens("main_screen",R.string.main)
    object LocationScreen: AppScreens("location_screen", R.string.location)
    object LocationListScreen: AppScreens("location_list_screen", R.string.location_list)
    object MapScreen: AppScreens("map_screen", R.string.map_list)
}

val items = listOf(
    AppScreens.LocationScreen,
    AppScreens.LocationListScreen,
    AppScreens.MapScreen
)