package com.sebastiancorradi.track.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun LoadingButton(
    color: Color,
    text: String,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val loadingMutableState = remember { mutableStateOf(loading) }
    if (loadingMutableState.value) {
        Box(
            modifier = modifier
                .clip(shape = CircleShape)
                .background(color = color)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        onClick()
                        loadingMutableState.value = false
                    })
                }
        )
    }
    else {
        Box(
            modifier = modifier
                .background(color = color)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        onClick()
                        loadingMutableState.value = true
                    })
                }
        )
        Text(text = text)

    }
}