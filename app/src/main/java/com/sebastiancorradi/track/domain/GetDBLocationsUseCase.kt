package com.sebastiancorradi.track.domain

import android.util.Log
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.repository.DBConnection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDBLocationsUseCase @Inject constructor(
    private val dbConnection: DBConnection,
) {

    operator fun invoke(deviceId:String): Flow<List<DBLocation>> {
        return dbConnection.getLocationFlow(deviceId)
    }
}