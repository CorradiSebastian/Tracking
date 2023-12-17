package com.sebastiancorradi.track.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.sebastiancorradi.track.services.LocationService
import com.sebastiancorradi.track.ui.components.checkLocationGranted
import com.sebastiancorradi.track.ui.components.hasLocationPermission
import com.sebastiancorradi.track.ui.components.subscribeToLocationUpdates
import com.sebastiancorradi.track.ui.components.unSuscribeToLocationUpdates

private lateinit var viewModel: LocationViewModel

@Composable
fun LocationScreen(_viewModel: LocationViewModel = viewModel()) {
    val context = LocalContext.current
    viewModel = _viewModel
    var location by remember { mutableStateOf("Your location") }

    // Create a permission launcher
    val requestPermissionLauncher = checkLocationGranted(
        {
            subscribeToLocationUpdates(context, ::locationUpdate)
        },
        {
            permissionDenied()
        },
    )

    setLifeCycleObserver()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (hasLocationPermission(context)) {
                    subscribeToLocationUpdates(context, ::locationUpdate)
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        ) {
            Text(text = "Allow Standard")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (hasLocationPermission(context)) {
                    startForegroundLocationService(context)
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        ) {
            Text(text = "Allow Foreground")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (hasLocationPermission(context)) {
                    Log.e("Sebastito", "click, luego tenia permisos")
                    viewModel.startLocationUpdates()
                } else {
                    // Request location permission
                    Log.e("Sebastito", "click, luego launchPermissionsLauncher")
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        ) {
            Text(text = "nuevo Foreground")
        }
        Spacer(modifier = Modifier.height(16.dp))
        //TODO check if this is ok
        Text(text = location.toString())
    }
}

private fun startForegroundLocationService(context: Context){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(context, LocationService::class.java)
        Log.e("Sebastito", "about to call StartForegroundService")// Build the intent for the service
        //context.startForegroundService(intent)
        context.startService(intent)
    }

}
@Composable
fun setLifeCycleObserver() {
    val TAG = "sebastrack"
    val context = LocalContext.current
    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "onCreate")
            }
            Lifecycle.Event.ON_START -> {
                Log.d(TAG, "On Start")
            }
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "On Resume")
            }
            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "On Pause")
            }
            Lifecycle.Event.ON_STOP -> {
                Log.d(TAG, "On Stop")

                unSuscribeToLocationUpdates(context, ::locationUpdate)
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "On Destroy")
            }
            else -> {}
        }
    }
}

fun locationUpdate(location: Location){
    //TODO develop what to do on updates
    Log.e(
        "sebastrack",
        "updated lat: ${location.latitude}, long: ${location.longitude}"
    )
}
private fun permissionDenied() {
    viewModel.permissionDenied()
}

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
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
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
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

