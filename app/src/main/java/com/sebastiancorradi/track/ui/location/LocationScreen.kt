package com.sebastiancorradi.track.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.navigation.AppScreens
import com.sebastiancorradi.track.services.ForegroundLocationService
import com.sebastiancorradi.track.store.UserStore
import com.sebastiancorradi.track.ui.locationlist.LocationListScreen


private lateinit var viewModel: LocationViewModel
val TAG = "LocationScreen"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(navController: NavController, onNavigateToList: () -> Unit, _viewModel: LocationViewModel = hiltViewModel()) {

    val context = LocalContext.current

    //es para que lo pueran referenciar desde afuera
    viewModel = _viewModel
    val state = _viewModel.mainScreenUIState.collectAsState()

    val store =  UserStore(context.applicationContext)

    val trackingFlow = remember {
        store.getTrackingStatus
    }

    val tracking = trackingFlow.collectAsState(initial = false).value
    // Create a permission launcher
    //TODO ver si hace falta algo visual que se hidrate desde el estado, para que lo dibuje de nuevo
    /*
    if (hasLocationAndPostPermissions(context)) {
        //subscribeToLocationUpdates(context, ::locationUpdate)
    } else {
        permissionDenied()
    }*/
    //TODO cambiar la logica, tener un flag en el estado para saber si tengo que pedir el permiso
    RequestPermissions(state.value.requestLocationPermission, state.value.requestNotificationPermission)

    setLifeCycleObserver()

    if(state.value.startForeground) {
        startForegroundLocationService(LocalContext.current, state.value.trackFrequencySecs)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(24.dp),
        ) {
            OutlinedTextField(
                value = if (tracking){
                    "Tracking"
                } else {
                    "Not Tracking"
                },
                readOnly = true,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.background(
                    if (tracking){
                        Color.Red
                    } else {
                        Color.Green
                    }
                ),
            )
        }
        Button(onClick = {
            stopForegroundLocationService(context)
            /*if (hasLocationAndPostPermissions(context)) {
                subscribeToLocationUpdates(context, ::locationUpdate)
            } else {
                // Request location permission
                viewModel.allowStandardClicked()

            }*/
        }) {
            Text(text = "Stop Foreground tracking")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {

            _viewModel.allowForegroundClicked()
        }) {
            Text(text = "Start Foreground Tracking")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Frequency",
            color = Color.Blue,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.value.trackFrequencySecs.toString(),
            maxLines = 2,
            //enabled = !tracking.value,
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(text = "update Frequency, default 10")
            },
            onValueChange = { newFrequency ->
                _viewModel.updatedfrequency(newFrequency)
            },
        )
        Spacer(modifier = Modifier.height(16.dp))

        //TODO check if this is ok
        Button(onClick = {

            val deviceId = (context.applicationContext as TrackApp).getDeviceID()
            _viewModel.locationsFlowRequested(deviceId)
        }) {
            Text(text = "getLocationsFLow")
        }
        Spacer(modifier = Modifier.height(16.dp))

        //TODO check if this is ok
        Button(onClick = {

            onNavigateToList()
        }) {
            Text(text = "go to list")
        }

        //------------------------------------------------
       // bottomBar = {
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
     //   }
        //------------------------------------------------
    }
}

private fun startForegroundLocationService(context: Context, frequency: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(context.applicationContext, ForegroundLocationService::class.java)
        intent.putExtra(ForegroundLocationService.FREQUENCY_SECS, frequency)
        context.startForegroundService(intent)
        viewModel.foregroundStarted()
    }
}

private fun stopForegroundLocationService(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val stopIntent = Intent(context, ForegroundLocationService::class.java).setAction(ForegroundLocationService.ACTION_STOP_UPDATES)

        context.stopService(stopIntent)
    //TODO update view
    viewModel.stopForeground()
    }
}
@Composable
fun setLifeCycleObserver() {
    //val context = LocalContext.current
    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
              //  Log.d(TAG, "onCreate")
            }

            Lifecycle.Event.ON_START -> {
              //  Log.d(TAG, "On Start")
            }

            Lifecycle.Event.ON_RESUME -> {
               // Log.d(TAG, "On Resume")
            }

            Lifecycle.Event.ON_PAUSE -> {
               // Log.d(TAG, "On Pause")
            }

            Lifecycle.Event.ON_STOP -> {
              //  Log.d(TAG, "On Stop")

                //unSuscribeToLocationUpdates(context, ::locationUpdate)
            }

            Lifecycle.Event.ON_DESTROY -> {
             //   Log.d(TAG, "On Destroy")
            }

            else -> {}
        }
    }
}

fun locationUpdate(location: Location) {
    //TODO develop what to do on updates
}

private fun permissionDenied() {
    viewModel.permissionDenied()
}

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        //TODO esto es cuando no tiene los permisos
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }.addOnFailureListener { exception ->
            // Handle location retrieval failure
            exception.printStackTrace()
        }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(requestLocation: Boolean = false, requestNotification: Boolean = false) {
    val permissions = mutableListOf<String>()
    if (requestLocation){
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    if (requestNotification){
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }
    if (permissions.size == 0)
        return

    val lifeCycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberMultiplePermissionsState(permissions = permissions){
        // tiene un mapa <String, boolean
        viewModel.permissionsGranted(it)

    }

    DisposableEffect(key1= lifeCycleOwner) {
        val observer = LifecycleEventObserver{ source, event ->
            when (event){
                Lifecycle.Event.ON_START -> {
                    permissionState.launchMultiplePermissionRequest()
                }

                else -> {

                }
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

}

/*@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    LocationScreen()
}*/

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Location : Screen(AppScreens.LocationScreen.route, R.string.location)
    object LocationList : Screen(AppScreens.LocationListScreen.route, R.string.locationList)
}

val items = listOf(
    Screen.Location,
    Screen.LocationList,
)