package com.sebastiancorradi.track.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sebastiancorradi.track.ui.location.LocationScreen
import com.sebastiancorradi.track.ui.locationlist.LocationListScreen
import com.sebastiancorradi.track.ui.main.MainScreen
import com.sebastiancorradi.track.ui.map.MapScreen
import com.sebastiancorradi.track.ui.splash.SplashScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = AppScreens.LocationScreen.route, Modifier.padding(innerPadding)) {
            composable(AppScreens.SplashScreen.route){
                SplashScreen(navController)
            }
            composable(AppScreens.MainScreen.route){
                MainScreen(onResumeClicked = {navController.navigate(AppScreens.LocationScreen.route)}
                )
            }
            composable(AppScreens.LocationScreen.route){
                LocationScreen(
                    //onNavigateToList = { navController.navigate(AppScreens.LocationListScreen.route) },
                    //onNavigateToMap = { navController.navigate(AppScreens.MapScreen.route) },
                )

            }
            composable(AppScreens.LocationListScreen.route){
                LocationListScreen()
            }
            composable(AppScreens.MapScreen.route){
                MapScreen()
            }

        }
    }

}