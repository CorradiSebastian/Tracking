package com.sebastiancorradi.track.domain

import android.util.Log
import com.sebastiancorradi.track.ui.main.MainScreenUIState

class AllowTrackingClicked {
    operator fun invoke(mainScreenUIState: MainScreenUIState, startForeground:Boolean): MainScreenUIState {
        val requestLocationPermission = !mainScreenUIState.locationPermissionGranted
        val requestNotificationPermission  = !mainScreenUIState.notificationPermissionGranted
        var newState = mainScreenUIState.copy(
                            requestLocationPermission = requestLocationPermission,
                            requestNotificationPermission = requestNotificationPermission,
                            startForeground = startForeground)

        try {
            if (mainScreenUIState.trackFrequencySecs.isEmpty() || mainScreenUIState.trackFrequencySecs.toInt() == 0)
                newState.trackFrequencySecs = "10"
        } catch (e: NumberFormatException){
            newState.trackFrequencySecs = "10"
        }
        return newState
    }
}