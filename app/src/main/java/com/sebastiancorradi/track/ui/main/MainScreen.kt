package com.sebastiancorradi.track.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.ui.components.LoadingButton
import com.sebastiancorradi.track.ui.theme.TrackTheme


@Composable
fun MainScreen( mainViewModel: MainViewModel = viewModel()){
    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()
    //mainViewModel = _mainViewModel
    TrackTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainContent(mainScreenUIState, mainViewModel)
        }
    }
}
@Composable
fun MainContent(mainScreenUIState: MainScreenUIState, mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ){
                /*LoadingButton(color = colorResource(R.color.buttonEnabled), text = "start") {
                    
                }*/
                ElevatedButton(
                    modifier = Modifier.background(colorResource(R.color.buttonEnabled)),
                    onClick = { mainViewModel.startTrackingClicked() },
                    //colors = ButtonColors(containerColor = colorResource(R.color.buttonEnabled)),
                ) {
                    if (mainScreenUIState.tracking)
                    Text("Stop Tracking") else
                        Text("Start Tracking")
                }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
    /*TrackTheme {
        MainContent(MainScreenUIState())
    }*/
}
