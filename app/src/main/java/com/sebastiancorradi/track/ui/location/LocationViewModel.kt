package com.sebastiancorradi.track.ui.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.domain.AllowTrackingClicked
import com.sebastiancorradi.track.domain.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.repository.DBConnection
import com.sebastiancorradi.track.services.ForegroundLocationServiceConnection
import com.sebastiancorradi.track.ui.main.MainScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val serviceConnection: ForegroundLocationServiceConnection
): ViewModel() {
    @Inject
    lateinit var allowTrackingUseCase: AllowTrackingClicked

    @Inject
    lateinit var permissionRequestUseCase: PermissionRequestUseCase

    @Inject
    lateinit var getDBLocationsUseCase: GetDBLocationsUseCase


    //private var _mainScreenUIState = mutableStateOf(MainScreenUIState())
    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()

    private lateinit var _dbLocationsFlow: Flow<List<DBLocation>>
    //val dbLocationsFlow: StateFlow<MainScreenUIState> = _dbLocationsFlow.asStateFlow()
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

    fun locationsFlowRequested(deviceId:String){
        getDBLocationsFlow(deviceId)
    }
    fun getDBLocationsFlow(deviceId: String){
        Log.e("Sebastrack", "antes de pedir el flow, deviceID: $deviceId")
        _dbLocationsFlow = getDBLocationsUseCase(deviceId)
        viewModelScope.launch {
            _dbLocationsFlow.collect { locations ->
                // Update DB, add latest location
                //Log.e("Sebastrack2", "locations modified. Size: ${locations.size}")
            }
        }
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