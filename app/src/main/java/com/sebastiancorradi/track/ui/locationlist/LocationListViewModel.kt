package com.sebastiancorradi.track.ui.locationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.data.LocationListUIState
import com.sebastiancorradi.track.domain.GetDBLocationsUseCase
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
class LocationListViewModel @Inject constructor(
    private val serviceConnection: ForegroundLocationServiceConnection
): ViewModel() {

    @Inject
    lateinit var getDBLocationsUseCase: GetDBLocationsUseCase

    private val _locationListUIState = MutableStateFlow(LocationListUIState())
    val locationListUIState: StateFlow<LocationListUIState> = _locationListUIState.asStateFlow()

    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()

    private lateinit var _dbLocationsFlow: Flow<List<LocationData>>

    fun getDBLocationsFlow(deviceId: String){
        _dbLocationsFlow = getDBLocationsUseCase(deviceId)
        viewModelScope.launch {
            _dbLocationsFlow.collect { locations ->
                _locationListUIState.value = locationListUIState.value.copy(locations = locations)// Update DB, add latest location
            }
        }
    }

    fun locationsFlowRequested(deviceId:String){
        getDBLocationsFlow(deviceId)
    }

    //fun getDBLocationsFlow(deviceId: String){
    //        _dbLocationsFlow = getDBLocationsUseCase(deviceId)
    //        viewModelScope.launch {
    //            _dbLocationsFlow.collect { locations ->
    //                // Update DB, add latest location
    //            }
    //        }
    //    }
}