package com.sebastiancorradi.track.domain

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import com.sebastiancorradi.track.ui.main.MainScreenUIState

class PermissionRequestUseCase {

    operator fun invoke(state: MainScreenUIState, permissions: Map<String, Boolean,>):MainScreenUIState {
        if (permissions.getValue(ACCESS_FINE_LOCATION) &&
            permissions.getValue(POST_NOTIFICATIONS)
            ){
            return state.copy(startForeground = false)
        }
        return state
    }
}