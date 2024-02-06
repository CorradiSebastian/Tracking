package com.sebastiancorradi.track.data

data class MapUIState(
    var locations: List<LocationData> = emptyList<LocationData>(),
    var focusOnLastPosition: Boolean = true
) {

}