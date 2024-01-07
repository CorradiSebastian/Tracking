package com.sebastiancorradi.track.domain

import android.location.Location
import android.util.Log
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.repository.DBConnection
import com.sebastiancorradi.track.repository.LocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(
    private val dbConnection: DBConnection,
) {

    operator fun invoke(location: Location, deviceId:String) {
        dbConnection.addLocation(DBLocation(location.latitude, location.longitude, location.time, deviceId))
    }
}