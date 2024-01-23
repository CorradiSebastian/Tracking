package com.sebastiancorradi.track.ui.location

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.domain.AllowTrackingClicked
import com.sebastiancorradi.track.domain.DeleteLocationsUseCase
import com.sebastiancorradi.track.domain.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.domain.StopTrackingUseCase
import com.sebastiancorradi.track.domain.UpdateFrequencyUseCase
import com.sebastiancorradi.track.services.ForegroundLocationServiceConnection
import com.sebastiancorradi.track.store.UserStore
import com.sebastiancorradi.track.ui.main.MainScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Sport { HIKE, RUN, TOURING_BICYCLE, E_TOURING_BICYCLE }
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val serviceConnection: ForegroundLocationServiceConnection
): ViewModel() {
    val TAG = "LocationViewModel"
    @Inject
    lateinit var allowTrackingUseCase: AllowTrackingClicked

    @Inject
    lateinit var stopTrackingUseCase: StopTrackingUseCase

    @Inject
    lateinit var permissionRequestUseCase: PermissionRequestUseCase

    @Inject
    lateinit var getDBLocationsUseCase: GetDBLocationsUseCase

    @Inject
    lateinit var updateFrequencyUseCase: UpdateFrequencyUseCase

    @Inject
    lateinit var deleteLocationsUseCase: DeleteLocationsUseCase

    @Inject
    lateinit var store : UserStore

    //private var _mainScreenUIState = mutableStateOf(MainScreenUIState())
    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()

    private lateinit var _dbLocationsFlow: Flow<List<LocationData>>
    //val dbLocationsFlow: StateFlow<MainScreenUIState> = _dbLocationsFlow.asStateFlow()
    fun permissionDenied(){
        //TODO update state with the error and act in consequence
    }

    fun allowStandardClicked(){
        _mainScreenUIState.value = allowTrackingUseCase(mainScreenUIState.value, startForeground = false)
    }

    fun allowForegroundClicked(){
        _mainScreenUIState.value = allowTrackingUseCase(mainScreenUIState.value, startForeground = true)
        //_mainScreenUIState.value = allowTrackingUseCase(mainScreenUIState.value, startForeground = false)
    }

    fun locationsFlowRequested(deviceId:String){
        getDBLocationsFlow(deviceId)


        data class Summary(val sport: Sport, val distance: Int)
        val sportStats = listOf(Summary(Sport.HIKE, 92),
            Summary(Sport.RUN, 77),
            Summary(Sport.TOURING_BICYCLE, 322),
            Summary(Sport.E_TOURING_BICYCLE, 656))

        val top = sportStats.sortedBy { it.distance }.last()

    }
    fun getDBLocationsFlow(deviceId: String){
        _dbLocationsFlow = getDBLocationsUseCase(deviceId)
        viewModelScope.launch {
            _dbLocationsFlow.collect { locations ->
                // Update DB, add latest location
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

    fun updatedfrequency(newFrequency: String) {
            val updatedFrequency = updateFrequencyUseCase(_mainScreenUIState.value.trackFrequencySecs, newFrequency)
            _mainScreenUIState.value = _mainScreenUIState.value.copy(trackFrequencySecs = updatedFrequency)
    }

    fun foregroundStarted() {
        //TODO hacer Usecase
        _mainScreenUIState.value = _mainScreenUIState.value.copy( startForeground = false)
    }

    fun stopForeground(deviceId: String) {
        _mainScreenUIState.value = _mainScreenUIState.value.copy(startForeground = false)
        /*runBlocking {
            store.saveTrackingStatus(false)
        }*/
    }

    @SuppressLint("NewApi")
    fun deleteLocationsRequested(deviceId: String) {
        deleteLocationsUseCase(deviceId)
    }

}