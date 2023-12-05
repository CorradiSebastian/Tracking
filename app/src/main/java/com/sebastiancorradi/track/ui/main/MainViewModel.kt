package com.sebastiancorradi.track.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sebastiancorradi.track.domain.StartTrackingUseCase
import com.sebastiancorradi.track.domain.StopTrackingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    //private var _mainScreenUIState = mutableStateOf(MainScreenUIState())
    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()
    fun startTrackingClicked(){
        _mainScreenUIState.update {
                currentState -> currentState.copy(tracking = !currentState.tracking)
        }
        if (_mainScreenUIState.value.tracking){
            StartTrackingUseCase()
        } else {
            StopTrackingUseCase()
        }
    }
}