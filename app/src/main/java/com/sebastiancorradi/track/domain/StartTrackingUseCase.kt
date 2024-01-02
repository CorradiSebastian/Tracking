package com.sebastiancorradi.track.domain

import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.sebastiancorradi.track.repository.LocationRepository
import javax.inject.Inject


class StartTrackingUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    operator fun invoke() {
        Log.e("Sebastrack", "usecase, starting location updates")
        locationRepository.startLocationUpdates()
    }
}