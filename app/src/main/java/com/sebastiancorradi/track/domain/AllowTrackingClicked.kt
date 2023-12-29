package com.sebastiancorradi.track.domain

import android.util.Log
import com.sebastiancorradi.track.ui.main.MainScreenUIState

class AllowTrackingClicked {
    operator fun invoke(mainScreenUIState: MainScreenUIState, startForeground:Boolean): MainScreenUIState {
        val requestLocationPermission = !mainScreenUIState.locationPermissionGranted
        val requestNotificationPermission  = !mainScreenUIState.notificationPermissionGranted
        return mainScreenUIState.copy(requestLocationPermission = requestLocationPermission, requestNotificationPermission = requestNotificationPermission, startForeground = startForeground)
    }
}