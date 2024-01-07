package com.sebastiancorradi.track.domain

import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.sebastiancorradi.track.repository.DBConnection
import com.sebastiancorradi.track.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class StartTrackingUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {

    operator fun invoke(deviceID: String): MutableStateFlow<Location?> {
        Log.e("Sebastrack", "usecase, starting location updates")
        Log.e("Sebastrack2", "startLocation called: $locationRepository")
        return locationRepository.startLocationUpdates()
    }
}