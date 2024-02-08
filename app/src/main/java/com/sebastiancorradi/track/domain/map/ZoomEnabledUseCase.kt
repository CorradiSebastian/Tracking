package com.sebastiancorradi.track.domain.map

import com.sebastiancorradi.track.data.MapUIState

class ZoomEnabledUseCase {
    operator fun invoke(mapUiState: MapUIState, zoomEnabled: Boolean): MapUIState {
        return if (zoomEnabled != mapUiState.zoomEnabled){
            mapUiState.copy(zoomEnabled = zoomEnabled)
        } else {
            mapUiState
        }
    }

}