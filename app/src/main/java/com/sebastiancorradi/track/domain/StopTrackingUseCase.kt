package com.sebastiancorradi.track.domain

import android.util.Log
import com.sebastiancorradi.track.repository.LocationRepository
import javax.inject.Inject


class StopTrackingUseCase  @Inject constructor(
    private val locationRepository: LocationRepository
)  {
    operator fun invoke() {
        locationRepository.stopLocationUpdates()
    }
}