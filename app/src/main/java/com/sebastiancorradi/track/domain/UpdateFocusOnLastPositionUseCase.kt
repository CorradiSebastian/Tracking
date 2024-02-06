package com.sebastiancorradi.track.domain

import com.sebastiancorradi.track.data.MapUIState

class UpdateFocusOnLastPositionUseCase {
    operator fun invoke(mapUiState: MapUIState, updatedFocusOnLastPosition: Boolean): MapUIState {
        return if (updatedFocusOnLastPosition != mapUiState.focusOnLastPosition){
            mapUiState.copy(focusOnLastPosition = updatedFocusOnLastPosition)
        } else {
            mapUiState
        }
    }
}