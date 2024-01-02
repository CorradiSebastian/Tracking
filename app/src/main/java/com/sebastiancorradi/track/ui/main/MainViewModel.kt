package com.sebastiancorradi.track.ui.main

import android.content.ServiceConnection
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastiancorradi.track.domain.StartTrackingUseCase
import com.sebastiancorradi.track.domain.StopTrackingUseCase
import com.sebastiancorradi.track.services.ForegroundLocationServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
//class MainViewModel @Inject constructor(private val serviceConnection: ForegroundLocationServiceConnection): ViewModel() {
class MainViewModel @Inject constructor(): ViewModel() {
/*@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceConnection: ForegroundLocationServiceConnection
): ViewModel(), ServiceConnection by serviceConnection {*/
    //private var _mainScreenUIState = mutableStateOf(MainScreenUIState())
    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()
    fun startTrackingClicked(){
        _mainScreenUIState.update {
            //TODO pasar a usecase
                currentState -> currentState.copy(tracking = !currentState.tracking)
        }
    }

    private fun startLocationUpdates() {
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
}