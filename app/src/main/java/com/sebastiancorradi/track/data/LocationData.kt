package com.sebastiancorradi.track.data

data class LocationData(
    val user: String,
    val ubicacion: DBLocation,
    ) {
}

data class DBLocation(
    val lat: Double,
    val long: Double,
    val date: Long,
    var deviceId:String,
)