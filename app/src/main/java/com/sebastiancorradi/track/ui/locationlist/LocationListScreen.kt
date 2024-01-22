package com.sebastiancorradi.track.ui.locationlist

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.store.UserStore
import com.sebastiancorradi.track.ui.components.LocationDetailCard


private val TAG = "LocationListScreen"
@Composable
fun LocationListScreen(viewModel: LocationListViewModel = hiltViewModel()) {

    val context = LocalContext.current

    // viewModel = _viewModel
    val state = viewModel.locationListUIState.collectAsState()

    val store =  UserStore(context.applicationContext)

    val trackingFlow = remember {
        store.getTrackingStatus
    }
    val tracking = trackingFlow.collectAsState(initial = false).value

    val locats = state.value.locations
    val locs = remember { listOf(LocationData("user1", DBLocation(1.0, 2.0, 234234L, "deviceID")),
        LocationData("user1", DBLocation(1.0, 2.0, 234234L, "deviceID"))) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                locats
            ) {
                //TODO reemplasar UserID con el valor q va
                LocationDetailCard(it)
            }
        }
        Button(onClick = {
            Toast.makeText(context, "list", Toast.LENGTH_LONG).show()
            val deviceId = (context.applicationContext as TrackApp).getDeviceID()
            viewModel.locationsFlowRequested(deviceId)
        }) {
            Text(text = "list")
        }
    }

}