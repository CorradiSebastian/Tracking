package com.sebastiancorradi.track.domain

import android.annotation.SuppressLint
import android.util.Log
import com.sebastiancorradi.track.data.EventType
import com.sebastiancorradi.track.repository.LocationRepository
import com.sebastiancorradi.track.store.UserStore
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class StopTrackingUseCase  @Inject constructor(
    private val locationRepository: LocationRepository,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val store : UserStore
)  {

    @SuppressLint("NewApi")
    operator fun invoke(deviceId: String) {
        val lastLocation = locationRepository.stopLocationUpdates()
        saveLocationUseCase(lastLocation, deviceId, EventType.STOP)
        runBlocking {
            store.saveTrackingStatus(false)
        }
    }
}