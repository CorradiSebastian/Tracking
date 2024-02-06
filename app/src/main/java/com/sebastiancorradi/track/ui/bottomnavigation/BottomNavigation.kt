package com.sebastiancorradi.track.ui.bottomnavigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.navigation.AppScreens

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Location : Screen(AppScreens.LocationScreen.route, R.string.location)
    object LocationList : Screen(AppScreens.LocationListScreen.route, R.string.location_list)
    object MapScreen : Screen(AppScreens.MapScreen.route, R.string.map_list)
}
val items = listOf(
    Screen.Location,
    Screen.LocationList,
    Screen.MapScreen
)
@Composable
fun createBottomNavigation(navController: NavController,) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize().padding(12.dp),
    ) {
        val (bottomBar) = createRefs()

        BottomNavigation(
            modifier = Modifier.constrainAs(bottomBar) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                BottomNavigationItem(icon = {
                    Icon(
                        Icons.Filled.Favorite, contentDescription = null
                    )
                },
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items

                            /*popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }*/

                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    })
            }
        }
    }
}