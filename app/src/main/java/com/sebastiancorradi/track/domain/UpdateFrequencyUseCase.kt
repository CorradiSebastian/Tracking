package com.sebastiancorradi.track.domain

import android.Manifest
import com.sebastiancorradi.track.repository.LocationRepository
import com.sebastiancorradi.track.ui.main.MainScreenUIState
import javax.inject.Inject

class UpdateFrequencyUseCase  @Inject constructor() {
    private val maxFrequency = 1000
    operator fun invoke(state: MainScreenUIState,newFrequency: String): MainScreenUIState {
        state.trackFrequencySecs = try {
            val result = newFrequency.toInt()
            if (result > maxFrequency)
                state.trackFrequencySecs
            else
                result.toString()
        } catch (e: NumberFormatException) {
            ""
        }
        return state
    }

}