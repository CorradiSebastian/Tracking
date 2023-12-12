package com.sebastiancorradi.track.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
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
                 // Permission granted, update the location
                 callbackGranted()
                 /*getCurrentLocation(context) { lat, long ->
                     location = "Latitude: $lat, Longitude: $long"
                 }*/
             } else {
                 callbackDenied()
             }
         })
 }

fun subscribeToLocationUpdates(context: Context, callback: (Location) -> Unit){
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
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        Log.e("sebastrack", "subscribing, no tenia permisos")
        return
    } else {
        Log.d("sebastrack", "Subscribing posta")
        val locationRequest  = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(30000)
            .build();

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )
    }
}

fun unSuscribeToLocationUpdates(context: Context, callback: (Location) -> Unit){
    Log.d("sebastrack", "unsubscribing.")
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