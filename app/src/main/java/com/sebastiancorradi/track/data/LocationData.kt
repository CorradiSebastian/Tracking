package com.sebastiancorradi.track.data

data class LocationData(
    val user: String? = null,
    val ubicacion: DBLocation? = null,
    ) {
}

data class DBLocation(
    val lat: Double? = null,
    val long: Double? = null,
    val date: Long? = null,
    var deviceId:String? = null,
)

