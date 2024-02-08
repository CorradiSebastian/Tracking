package com.sebastiancorradi.track.domain.db

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.EventType
import com.sebastiancorradi.track.repository.DBConnection
import javax.inject.Inject

class DeleteLocationsUseCase @Inject constructor(
    private val dbConnection: DBConnection,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(deviceId:String) {
        dbConnection.deleteLocations(deviceId)
    }
}