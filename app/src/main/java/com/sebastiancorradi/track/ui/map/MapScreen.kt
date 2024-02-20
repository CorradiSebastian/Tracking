package com.sebastiancorradi.track.ui.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.currentCameraPositionState
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.data.MapUIState

val TAG = "MapScreen"
private lateinit var viewModel: MapViewModel
private lateinit var state:MapUIState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(_viewModel: MapViewModel = hiltViewModel()) {

    viewModel = _viewModel
    state = viewModel.mapUIState.collectAsState().value
    val locations = state.locations
    val context = LocalContext.current
    remember() {
        val deviceId = (context.applicationContext as TrackApp).getDeviceID()
        viewModel.locationsFlowRequested(deviceId)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (row1, text) = createRefs()

        Row(modifier = Modifier.constrainAs(row1) {
            top.linkTo(parent.top, margin = 5.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text("Hello")
        }

        ShowMap(locations)
    }
}


@Composable
fun ShowMap(locations: List<LocationData>){
    var centerOnLastPosition = state.focusOnLastPosition
    Log.e(TAG, "showMap")
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            uiSettings = uiSettings,
            //cameraPositionState =  cameraPositionState,
        ){
            for (location in locations) {
                location.ubicacion?.let {
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                it.lat?:0.0,
                                it.long?:0.0
                            )
                        ), title = it.eventType.toString(), snippet = "Marker"
                    )
                }
            }
            if (centerOnLastPosition) {
                var lastCameraPosition = if (locations.lastIndex >= 0) {
                    var lastLocation: LocationData? = locations.get(locations.lastIndex)
                    val lastPos = LatLng(
                        lastLocation?.ubicacion?.lat ?: 0.0, lastLocation?.ubicacion?.long ?: 0.0
                    )
                    var cameraPosition = CameraPosition.fromLatLngZoom(lastPos, 10f)
                    CameraPositionState(cameraPosition)
                } else {
                    var cameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
                    CameraPositionState(cameraPosition)
                }
                currentCameraPositionState.move(
                    CameraUpdateFactory.newCameraPosition(
                        lastCameraPosition.position
                    )
                )
                currentCameraPositionState.move(CameraUpdateFactory.zoomTo(19F))
            }
        }
        Log.e(TAG, "focus value: ${state.focusOnLastPosition}")
        Column {
            SwitchWithText(label = "Zoom enabled", callback = { checked ->
                uiSettings = uiSettings.copy(zoomControlsEnabled = checked,
                                                scrollGesturesEnabled = checked,
                                                zoomGesturesEnabled = checked,
                                                rotationGesturesEnabled = checked,)
                viewModel.zoomEnabled(checked, )
            }, viewModel.mapUIState.collectAsState().value.zoomEnabled)
            Spacer(modifier = Modifier.height(5.dp))
            SwitchWithText(label = "Center on last position", callback = { checked ->
                //centerOnLastPosition = checked
                viewModel.focusOnLastPositionUpdated(checked)
            }, viewModel.mapUIState.collectAsState().value.focusOnLastPosition)
        }
   // }
}

@Composable
fun BasicSwitch(callback: (checked: Boolean) -> Unit, checked: Boolean) {
    //var checked by remember { mutableStateOf(true) }
    //var checked = viewModel.mapUIState.collectAsState().value.focusOnLastPosition
    Switch(
        checked = checked,
        onCheckedChange = {
            callback(it)
        },
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun SwitchWithText(label: String, callback: (checked: Boolean) -> Unit, checked: Boolean) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
                .background(Color(0x80000000))
                .padding(horizontal = 5.dp),
        ) {
            Text(text =  label, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(5.dp))
            BasicSwitch(callback, checked)
        }
    }
}