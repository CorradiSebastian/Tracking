package com.sebastiancorradi.track.domain

import com.sebastiancorradi.track.repository.LocationRepository
import javax.inject.Inject

class UpdateFrequencyUseCase  @Inject constructor() {
    private val maxFrequency = 1000
    operator fun invoke(oldFrequency: String, newFrequency: String): String {
        return try {
            val result = newFrequency.toInt()
            if (result > maxFrequency)
                oldFrequency
            else
                result.toString()
        } catch (e: NumberFormatException) {
            ""
        }
    }
}