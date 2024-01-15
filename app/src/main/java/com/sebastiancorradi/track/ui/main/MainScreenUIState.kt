package com.sebastiancorradi.track.ui.main

import com.sebastiancorradi.track.data.LocationData

data class MainScreenUIState(
    var tracking: Boolean = false,
    var locationPermissionGranted: Boolean = false,
    var requestLocationPermission: Boolean = false,
    var notificationPermissionGranted: Boolean = false,
    var requestNotificationPermission: Boolean = false,
    var startForeground: Boolean = false,
    var startStandard:Boolean = false,
    var foregroundRunning:Boolean = false,
    //var locations:List<LocationData>? = null

    var trackFrequencySecs:Int = 10,
) {

}