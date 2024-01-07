package com.sebastiancorradi.track.ui.location

import androidx.lifecycle.ViewModel
import com.sebastiancorradi.track.domain.AllowTrackingClicked
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.repository.DBConnection
import com.sebastiancorradi.track.services.ForegroundLocationServiceConnection
import com.sebastiancorradi.track.ui.main.MainScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val serviceConnection: ForegroundLocationServiceConnection
): ViewModel() {
    @Inject
    lateinit var allowTrackingUseCase: AllowTrackingClicked

    @Inject
    lateinit var permissionRequestUseCase: PermissionRequestUseCase

    //private var _mainScreenUIState = mutableStateOf(MainScreenUIState())
    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()
    fun permissionDenied(){
        //TODO update state with the error and act in consequence
    }

    fun allowStandardClicked(){
        _mainScreenUIState.value = allowTrackingUseCase(mainScreenUIState.value, startForeground = false)
    }

    fun allowForegroundClicked(){
        val newValue = allowTrackingUseCase(mainScreenUIState.value, startForeground = true)
        //_mainScreenUIState.value = allowTrackingUseCase(mainScreenUIState.value, foreground = true)
        _mainScreenUIState.value = newValue
    }

    fun testDB(){
        val dbRepository = DBConnection()
        dbRepository.testAddSomething()
    }
    fun startLocationUpdates() {
        //serviceConnection.service?.startLocationUpdates()
        // Store that the user turned on location updates.
        // It's possible that the service was not connected for the above call. In that case, when
        // the service eventually starts, it will check the persisted value and react appropriately.
        /*viewModelScope.launch {
            locationPreferences.setLocationTurnedOn(true)
        }*/
    }

    private fun stopLocationUpdates() {
       //serviceConnection.service?.stopLocationUpdates()
        // Store that the user turned off location updates.
        // It's possible that the service was not connected for the above call. In that case, when
        // the service eventually starts, it will check the persisted value and react appropriately.
        /*viewModelScope.launch {
            locationPreferences.setLocationTurnedOn(false)
        }*/
    }

    fun permissionsGranted(permissions: Map<String, Boolean>) {
        _mainScreenUIState.value = permissionRequestUseCase(_mainScreenUIState.value, permissions)
    }
}