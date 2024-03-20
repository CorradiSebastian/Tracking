package com.sebastiancorradi.track.ui.main

import com.sebastiancorradi.track.data.LocationData

data class MainScreenUIState(
    var tracking: Boolean = false,
    var locationPermissionGranted: Boolean = false,
    var requestLocationPermission: Boolean = true,
    var notificationPermissionGranted: Boolean = false,
    var requestNotificationPermission: Boolean = true,
    var startForeground: Boolean = false,
    var startStandard:Boolean = false,
    var foregroundRunning:Boolean = false,
    var resumeClicked:Boolean = false,
    //var locations:List<LocationData>? = null

    var trackFrequencySecs:String = "10",
) {
}