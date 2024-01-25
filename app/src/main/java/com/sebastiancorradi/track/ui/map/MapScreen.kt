package com.sebastiancorradi.track.ui.map

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.sebastiancorradi.track.ui.location.LocationViewModel
import com.sebastiancorradi.track.ui.locationlist.LocationListViewModel

private val TAG = "MapScreen"




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: LocationListViewModel = hiltViewModel()) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize().padding(12.dp),
    ) {
        val (row1, text) = createRefs()

        Row(modifier = Modifier.constrainAs(row1) {
            top.linkTo(parent.top, margin = 5.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text("Hello")
        }

        ShowMap2()
    }
}

@Composable
fun ShowMap() {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )
    }
}

@Composable
fun ShowMap2(){
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }
    val singapore = LatLng(1.35, 103.87)


    //Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            uiSettings = uiSettings
        ){
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )
   // }
}