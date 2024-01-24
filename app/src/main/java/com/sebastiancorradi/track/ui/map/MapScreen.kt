package com.sebastiancorradi.track.ui.map

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
    }
}