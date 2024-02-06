package com.sebastiancorradi.track.repository

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) {
    private val callback = Callback()


    //TODO check is  is ok to have a Flow in the repository
    private val _isReceivingUpdates = MutableStateFlow(false)
    val isReceivingLocationUpdates = _isReceivingUpdates.asStateFlow()

    private val _lastLocationFlow = MutableStateFlow<Location?>(null)
    val lastLocation = _lastLocationFlow.asStateFlow()

    @SuppressLint("MissingPermission") // Only called when holding location permission.
    fun startLocationUpdates(frequencyMillis:Long):MutableStateFlow<Location?> {

        val request  = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, frequencyMillis)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(15000)
            .build();
        // Note: For this sample it's fine to use the main looper, so our callback will run on the
        // main thread. If your callback will perform any intensive operations (writing to disk,
        // making a network request, etc.), either change to a background thread from the callback,
        // or create a HandlerThread and pass its Looper here instead.
        // See https://developer.android.com/reference/android/os/HandlerThread.
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )
        _isReceivingUpdates.value = true
        return _lastLocationFlow
    }

    @SuppressLint("MissingPermission")
    fun stopLocationUpdates():Location? {
        //val lastLocation = fusedLocationProviderClient.lastLocation.result
        fusedLocationProviderClient.removeLocationUpdates(callback)
        _isReceivingUpdates.value = false
        val lastLocation = _lastLocationFlow.value
        _lastLocationFlow.value = null
        return lastLocation
    }

    private inner class Callback : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let {
                //saveLocation(it)
                _lastLocationFlow.value = result.lastLocation
            }
        }
    }

}
