package com.sebastiancorradi.track.domain

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.EventType
import com.sebastiancorradi.track.repository.DBConnection
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject


class SaveLocationUseCase @Inject constructor(
    private val dbConnection: DBConnection,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(location: Location?, deviceId:String, eventType: EventType) {
        val time = System.currentTimeMillis()
        dbConnection.addLocation(DBLocation(location?.latitude?:0.0, location?.longitude?:0.0, time, deviceId, eventType))
    }
}