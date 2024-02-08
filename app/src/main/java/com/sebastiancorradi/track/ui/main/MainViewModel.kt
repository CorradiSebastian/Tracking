package com.sebastiancorradi.track.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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


}