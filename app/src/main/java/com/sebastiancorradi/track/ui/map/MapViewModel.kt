package com.sebastiancorradi.track.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.data.MapUIState
import com.sebastiancorradi.track.domain.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.UpdateFocusOnLastPositionUseCase
import com.sebastiancorradi.track.domain.ZoomEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var getDBLocationsUseCase: GetDBLocationsUseCase

    @Inject
    lateinit var updateFocusOnLastPositionUseCase: UpdateFocusOnLastPositionUseCase
    @Inject
    lateinit var zoomEnabledUseCase: ZoomEnabledUseCase

    private lateinit var _dbLocationsFlow: Flow<List<LocationData>>

    /*private val _locationListUIState = MutableStateFlow(LocationListUIState())
    val locationListUIState: StateFlow<LocationListUIState> = _locationListUIState.asStateFlow()*/

    private val _mapUIState = MutableStateFlow(MapUIState())
    val mapUIState: StateFlow<MapUIState> = _mapUIState.asStateFlow()

    fun getDBLocationsFlow(deviceId: String): Flow<List<LocationData>> {
        _dbLocationsFlow = getDBLocationsUseCase(deviceId)
        viewModelScope.launch {
            _dbLocationsFlow.collect { locations ->
                _mapUIState.value = mapUIState.value.copy(locations = locations)// Update DB, add latest location
            }
        }
        return _dbLocationsFlow
    }

    fun locationsFlowRequested(deviceId:String): Flow<List<LocationData>> {
        return getDBLocationsFlow(deviceId)
    }

    fun focusOnLastPositionUpdated(autoFocus:Boolean) {
        _mapUIState.value = updateFocusOnLastPositionUseCase(_mapUIState.value, autoFocus)
    }

    fun zoomEnabled(zoomEnabled:Boolean) {
        _mapUIState.value = zoomEnabledUseCase(_mapUIState.value, zoomEnabled)
    }
}