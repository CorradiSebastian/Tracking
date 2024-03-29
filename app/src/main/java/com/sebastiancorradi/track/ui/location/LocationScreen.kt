package com.sebastiancorradi.track.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.services.ForegroundLocationService
import com.sebastiancorradi.track.store.UserStore
import com.sebastiancorradi.track.ui.components.HyperlinkText


private lateinit var viewModel: LocationViewModel
val TAG = "LocationScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    _viewModel: LocationViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    //es para que lo pueran referenciar desde afuera
    viewModel = _viewModel
    val state = _viewModel.mainScreenUIState.collectAsState()

    val store = UserStore(context.applicationContext)

    val trackingFlow = remember {
        store.getTrackingStatus
    }

    val tracking = trackingFlow.collectAsState(initial = false).value

    RequestPermissions(
        state.value.requestLocationPermission, state.value.requestNotificationPermission
    )

    if (state.value.startForeground) {
        startForegroundLocationService(context, state.value.trackFrequencySecs)
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        val (row1, trackingRow, text, outLText, button3, link) = createRefs()

        Row(modifier = Modifier.constrainAs(row1) {
            top.linkTo(parent.top, margin = 5.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            OutlinedTextField(
                value = if (tracking) {
                    "Tracking"
                } else {
                    "Not Tracking"
                },
                readOnly = true,
                onValueChange = {},
                enabled = false,
                modifier = Modifier
                    .background(
                        if (tracking) {
                            Color.Red
                        } else {
                            Color.Green
                        }
                    )
                    .fillMaxWidth(),
            )
        }
        Row(
            modifier = Modifier
                .constrainAs(trackingRow) {
                    top.linkTo(row1.bottom, margin = 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                stopForegroundLocationService(context)
            }) {
                Text(text = "Stop Foreground tracking")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                _viewModel.allowForegroundClicked()
            }) {
                Text(text = "Start Foreground Tracking")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(modifier = Modifier.constrainAs(text) {
            top.linkTo(trackingRow.bottom, margin = 5.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, text = "Frequency (secs)", color = Color.Blue, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.constrainAs(outLText) {
                top.linkTo(text.bottom, margin = 5.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            value = state.value.trackFrequencySecs,
            maxLines = 2,
            //enabled = !tracking.value,
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(text = "update Frequency, default 10")
            },
            onValueChange = { newFrequency ->
                _viewModel.updatedfrequency(newFrequency)
            },
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(modifier = Modifier.constrainAs(button3) {
            top.linkTo(outLText.bottom, margin = 5.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, onClick = {
            val deviceId = (context.applicationContext as TrackApp).getDeviceID()
            _viewModel.deleteLocationsRequested(deviceId)
        }) {
            Text(text = "delete locations")
        }
        Spacer(modifier = Modifier.height(16.dp))
        HyperlinkText(fullText = "You can find the privacy policy here ",
            linkText = listOf("here"),
            hyperlinks = listOf("https://raw.githubusercontent.com/CorradiSebastian/Tracking/main/privacyPolicy.txt"),
            modifier = Modifier.constrainAs(link) {
                top.linkTo(button3.bottom, margin = 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            color = Color.Gray
        )

        //   }
        //------------------------------------------------
    }
}

private fun startForegroundLocationService(context: Context, frequency: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        viewModel.foregroundStarted()
        val intent = Intent(context.applicationContext, ForegroundLocationService::class.java)
        try {
            intent.putExtra(ForegroundLocationService.FREQUENCY_SECS, frequency.toLong())
        } catch (e: Exception) {
            intent.putExtra(ForegroundLocationService.FREQUENCY_SECS, 10L)
        }
        context.startForegroundService(intent)
    }
}

private fun stopForegroundLocationService(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val stopIntent = Intent(context, ForegroundLocationService::class.java).setAction(
            ForegroundLocationService.ACTION_STOP_UPDATES
        )

        context.stopService(stopIntent)

        val deviceId = (context.applicationContext as TrackApp).getDeviceID()
        viewModel.stopForeground(deviceId)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(requestLocation: Boolean = false, requestNotification: Boolean = false) {
    val permissions = mutableListOf<String>()
    if (requestLocation) {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
    }
    if (requestNotification) {
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }
    if (permissions.size == 0) return

    val lifeCycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberMultiplePermissionsState(permissions = permissions) {
        // tiene un mapa <String, boolean
        viewModel.permissionsGranted(it)

    }

    DisposableEffect(key1 = lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchMultiplePermissionRequest()
                }

                else -> {

                }
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

}





