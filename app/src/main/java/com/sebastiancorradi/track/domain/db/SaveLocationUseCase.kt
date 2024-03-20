package com.sebastiancorradi.track.domain.db

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.EventType
import com.sebastiancorradi.track.repository.DBConnection
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveLocationUseCase @Inject constructor(
    private val dbConnection: DBConnection,
) {
    private val TAG = "SaveLocationUseCase"
    var shouldSendStart = true

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(location: Location?, deviceId:String, eventType: EventType) {
        Log.e(TAG, "SaveLocationUsecasae, location: $location")
        val time = System.currentTimeMillis()
        val notValidLocation = invalidLocation(location)
        if (notValidLocation && eventType == EventType.START){
            shouldSendStart = true
        }
        var type = eventType
        if (!notValidLocation) {
            if (shouldSendStart && eventType != EventType.STOP){
                shouldSendStart = false
                type = EventType.START
            }
            dbConnection.addLocation(
                DBLocation(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0,
                    time,
                    deviceId,
                    type
                )
            )
        }
        if (eventType == EventType.STOP){
            shouldSendStart = true
        }
    }

    private fun invalidLocation(location: Location?): Boolean {
        return (location == null) ||
                (location.latitude == 0.0) ||
                (location.longitude == 0.0 )
    }
}