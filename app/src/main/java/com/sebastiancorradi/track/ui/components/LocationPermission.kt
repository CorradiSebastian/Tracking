package com.sebastiancorradi.track.ui.components

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


fun hasLocationAndPostPermissions(context: Context): Boolean {
    return hasPermission(context, ACCESS_FINE_LOCATION) &&
            hasPermission(context, POST_NOTIFICATIONS)
}

fun hasPermission(context: Context, permission:String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions2(requestLocation: Boolean = false, requestNotification: Boolean = false) {
    val permissions = mutableListOf<String>()
    if (requestLocation){
        permissions.add(ACCESS_FINE_LOCATION)
        permissions.add(ACCESS_COARSE_LOCATION)
    }
    if (requestNotification){
        permissions.add(POST_NOTIFICATIONS)
    }
    if (permissions.size == 0)
        return

    val lifeCycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberMultiplePermissionsState(permissions = permissions){
       // tiene un mapa <String, boolean

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

fun requestPermissions(context: Context, requestPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

}


@Composable
 fun checkLocationGranted(
    callbackGranted: () -> Unit,
    callbackDenied: () -> Unit,
    ): ManagedActivityResultLauncher<String, Boolean> {
     return rememberLauncherForActivityResult(
         contract = ActivityResultContracts.RequestPermission(),
         onResult = { isGranted: Boolean ->
             if (isGranted) {
                 callbackGranted()
             } else {
                 callbackDenied()
             }
         })
 }

/*fun subscribeToLocationUpdates(context: Context, callback: (Location) -> Unit){
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        //TODO esto es cuando no tiene los permisos
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    } else {
        //val locationRequest  = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        val locationRequest  = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(30000)
            .build();
        val task = fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )
    }
}*/

fun unSuscribeToLocationUpdates(context: Context, callback: (Location) -> Unit){
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val removeTask = fusedLocationProviderClient.removeLocationUpdates(callback)
    removeTask.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("sebastrack", "Location Callback removed.")
        } else {
            Log.d("sebastrack", "Failed to remove Location Callback.")
        }
    }
}

