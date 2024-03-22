package com.sebastiancorradi.track.domain.db

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.EventType
import com.sebastiancorradi.track.repository.DBConnection
import javax.inject.Inject
import javax.inject.Singleton

sealed class ErrorMessage(val message: String, @StringRes val resourceId: Int) {
    object OkMessage : ErrorMessage("ok", R.string.ok_message)
    object MaxLocationsReached :
        ErrorMessage("Max locations reached", R.string.max_locations_reached)
}

@Singleton
class SaveLocationUseCase @Inject constructor(
    private val dbConnection: DBConnection,
) {


    private val TAG = "SaveLocationUseCase"
    var shouldSendStart = true

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(location: Location?, deviceId: String, eventType: EventType): ErrorMessage {
        val time = System.currentTimeMillis()
        val notValidLocation = invalidLocation(location)
        if (notValidLocation && eventType == EventType.START) {
            shouldSendStart = true
        }
        var type = eventType
        val locationCount = dbConnection.getLocationCount()
        val canAddLocation = dbConnection.getLocationCount() < 11
        if ((!notValidLocation) && (canAddLocation)) {
            if (shouldSendStart && eventType != EventType.STOP) {
                shouldSendStart = false
                type = EventType.START
            }
            dbConnection.addLocation(
                DBLocation(
                    location?.latitude ?: 0.0, location?.longitude ?: 0.0, time, deviceId, type
                )
            )
            if (eventType == EventType.STOP) {
                shouldSendStart = true
            }
            return ErrorMessage.OkMessage
        } else {
            return ErrorMessage.MaxLocationsReached
        }
        if (eventType == EventType.STOP) {
            shouldSendStart = true
        }
    }

    private fun invalidLocation(location: Location?): Boolean {
        return (location == null) || (location.latitude == 0.0) || (location.longitude == 0.0)
    }
}