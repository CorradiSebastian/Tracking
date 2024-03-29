package com.sebastiancorradi.track.ui.locationlist

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.store.UserStore
import com.sebastiancorradi.track.ui.components.LocationDetailCard


private val TAG = "LocationListScreen"
@Composable
fun LocationListScreen(
                       viewModel: LocationListViewModel = hiltViewModel()) {

    val context = LocalContext.current

    // viewModel = _viewModel
    val state = viewModel.locationListUIState.collectAsState()

    val store =  UserStore(context.applicationContext)

    val trackingFlow = remember {
        store.getTrackingStatus
    }
    val tracking = trackingFlow.collectAsState(initial = false).value

    val locats = state.value.locations
    val deviceId = (context.applicationContext as TrackApp).getDeviceID()
    remember {
        viewModel.locationsFlowRequested(deviceId)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        val (recyclerColumn, listButton, bottomBar) = createRefs()

        if (locats?.size == 0){
            Text("loading")
        } else {
            LazyColumn(modifier = Modifier.constrainAs(recyclerColumn) {
                top.linkTo(parent.top, margin = 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                //bottom.linkTo(listButton.top)
            }) {
                items(
                    locats
                ) {
                    LocationDetailCard(it)
                }
            }
        }
        /*
        Button(modifier = Modifier
            .constrainAs(listButton) {
                top.linkTo(recyclerColumn.bottom, margin = 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(bottomBar.top)

            }
            .wrapContentHeight(), onClick = {
            Toast.makeText(context, "list", Toast.LENGTH_LONG).show()
            val deviceId = (context.applicationContext as TrackApp).getDeviceID()
            viewModel.locationsFlowRequested(deviceId)
        }) {
            Text(text = "list")
        }*/

    }

}