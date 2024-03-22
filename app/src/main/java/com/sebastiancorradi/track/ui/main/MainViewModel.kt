package com.sebastiancorradi.track.ui.main

import androidx.lifecycle.ViewModel
import com.sebastiancorradi.track.domain.db.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.mainscreen.ResumeClickedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var resumeClickedUseCase: ResumeClickedUseCase

    private val _mainScreenUIState = MutableStateFlow(MainScreenUIState())
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState.asStateFlow()
}