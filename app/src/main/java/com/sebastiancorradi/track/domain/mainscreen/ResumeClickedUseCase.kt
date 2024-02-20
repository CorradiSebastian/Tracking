package com.sebastiancorradi.track.domain.mainscreen

import android.Manifest
import com.sebastiancorradi.track.ui.main.MainScreenUIState

class ResumeClickedUseCase {

    operator fun invoke(state: MainScreenUIState, clicked:Boolean):MainScreenUIState {
        if (state.resumeClicked != clicked)
        {
            return state.copy(resumeClicked = clicked)
        }
        return state
    }
}