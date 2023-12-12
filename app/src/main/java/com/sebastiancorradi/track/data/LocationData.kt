package com.sebastiancorradi.track.data

data class LocationData(
    val id: Int,
    val user: String,
    val lat: Double,
    val long: Double,
    val dateTime: String) {
}