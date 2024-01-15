package com.sebastiancorradi.track.ui.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.services.ForegroundLocationService
import com.sebastiancorradi.track.store.UserStore
import kotlinx.coroutines.flow.Flow


private lateinit var viewModel: LocationViewModel
val TAG = "LocationScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(_viewModel: LocationViewModel = viewModel()) {
    val context = LocalContext.current

    viewModel = _viewModel
    val state = viewModel.mainScreenUIState.collectAsState()

    val store = UserStore(context)
    val tracking = store.getTrackingStatus.collectAsState(initial = false)

    // Create a permission launcher
    //TODO ver si hace falta algo visual que se hidrate desde el estado, para que lo dibuje de nuevo
    /*
    if (hasLocationAndPostPermissions(context)) {
        //subscribeToLocationUpdates(context, ::locationUpdate)
    } else {
        permissionDenied()
    }*/
    //TODO cambiar la logica, tener un flag en el estado para saber si tengo que pedir el permiso
    RequestPermissions3(state.value.requestLocationPermission, state.value.requestNotificationPermission)

    setLifeCycleObserver()

    if(state.value.startForeground) {
        startForegroundLocationService(LocalContext.current)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.allowStandardClicked()
            /*if (hasLocationAndPostPermissions(context)) {
                subscribeToLocationUpdates(context, ::locationUpdate)
            } else {
                // Request location permission
                viewModel.allowStandardClicked()

            }*/
        }) {
            Text(text = "Allow Standard")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            /*if (hasLocationAndPostPermissions(context)) {
                startForegroundLocationService(context)
            } else {
                // Request location permission
                //TODO pedir permsos,
            // requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)


            }*/
            viewModel.allowForegroundClicked()
        }) {
            Text(text = "Allow Foreground")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Log.e(TAG, "tracking vale: ${tracking.value}")
        Log.e(TAG, "trackingLargo vale: ${state.value.trackFrequencySecs.toString()}")
        Log.e(TAG, "enabled: ${!tracking.value}")
        Text(text = "Frequency",
            color = Color.Blue,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.height(16.dp),
            value = state.value.trackFrequencySecs.toString(),
            maxLines = 2,
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(text = "update Frequency")
            },
           // value = viewModel.mainScreenUIState.collectAsState().value.trackFrequencySecs.toString(),
            onValueChange = { newFrequency ->
                _viewModel.updatedfrequency(newFrequency)
            },
            //TODO intentar sin esta opcion y validar en usecase
            //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //TODO check if this is ok
        Button(onClick = {

            val deviceId = (context.applicationContext as TrackApp).getDeviceID()
            _viewModel.locationsFlowRequested(deviceId)
        }) {
            Text(text = "getLocationsFLow")
        }

    }
}

private fun startForegroundLocationService(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(context.applicationContext, ForegroundLocationService::class.java)
        context.startForegroundService(intent)
    }

}

@Composable
fun setLifeCycleObserver() {
    //val context = LocalContext.current
    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "onCreate")
            }

            Lifecycle.Event.ON_START -> {
                Log.d(TAG, "On Start")
            }

            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "On Resume")
                Log.e("Sebastrack", "onresume: state: ${viewModel.mainScreenUIState.value}")
            }

            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "On Pause")
            }

            Lifecycle.Event.ON_STOP -> {
                Log.d(TAG, "On Stop")

                //unSuscribeToLocationUpdates(context, ::locationUpdate)
            }

            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "On Destroy")
            }

            else -> {}
        }
    }
}

fun locationUpdate(location: Location) {
    //TODO develop what to do on updates
    Log.e(
        "sebastrack", "updated lat: ${location.latitude}, long: ${location.longitude}"
    )
}

private fun permissionDenied() {
    viewModel.permissionDenied()
}

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        //TODO esto es cuando no tiene los permisos
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.e("Sebastrack", "getLastLocation success")
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }.addOnFailureListener { exception ->
            // Handle location retrieval failure
            exception.printStackTrace()
            Log.e("Sebastrack", "getLastLocation failure: $exception")
        }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions3(requestLocation: Boolean = false, requestNotification: Boolean = false) {
    val permissions = mutableListOf<String>()
    if (requestLocation){
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    if (requestNotification){
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }
    if (permissions.size == 0)
        return

    val lifeCycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberMultiplePermissionsState(permissions = permissions){
        // tiene un mapa <String, boolean
        viewModel.permissionsGranted(it)

    }

    DisposableEffect(key1= lifeCycleOwner) {
        val observer = LifecycleEventObserver{ source, event ->
            when (event){
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

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    LocationScreen()
}
