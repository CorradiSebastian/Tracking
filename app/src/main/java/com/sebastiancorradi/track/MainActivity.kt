package com.sebastiancorradi.track

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sebastiancorradi.track.navigation.AppNavigation
import com.sebastiancorradi.track.navigation.AppScreens
import com.sebastiancorradi.track.services.ForegroundLocationService
import com.sebastiancorradi.track.ui.components.LocationProviderChangedReceiver
import com.sebastiancorradi.track.ui.location.LocationViewModel
import com.sebastiancorradi.track.ui.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private lateinit var locationRequestLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val TAG = "MainActivity"

    private lateinit var auth: FirebaseAuth
    private val locationViewModel: LocationViewModel by viewModels()
    //private val locationViewModel: LocationViewModel by hiltNavGraphViewModels(R.id.my_graph)

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onResume() {
        super.onResume()
        auth = Firebase.auth
        if (auth.currentUser == null) {
            //LAUNCH THE LOGIN SCREEN
            setContent {
                val navController = rememberNavController()
                MainScreen({navController.navigate(AppScreens.LocationScreen.route)})
            }
            return
        } else {
            setContent {
                //val isLocationEnabled by locationViewModel.isLocationEnabled.collectAsStateWithLifecycle()
                val isLocationEnabled by locationViewModel.isLocationEnabled.collectAsState()
                if (!isLocationEnabled) {
                    //locationViewModel.enableLocationRequest(this@MapsActivity) {//Call this if GPS is OFF.
                    enableLocationRequest(this) {//Call this if GPS is OFF.
                        locationRequestLauncher.launch(it)//Launch it to show the prompt.
                    }
                }
                AppNavigation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLocationRequestLauncher()
        registerBroadcastReceiver()

    }

    // Choose authentication providers
    val providers = arrayListOf(
        //AuthUI.IdpConfig.EmailBuilder().build(),
        //AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )

    fun signIn() {// Create and launch sign-in intent
        val signInIntent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        //Auth will be checked in OnResume method
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            // Successfully signed in
            //val user = FirebaseAuth.getInstance().currentUser

        } else {
            //TODO
        }
    }

    fun enableLocationRequest(
        context: Context,
        makeRequest: (intentSenderRequest: IntentSenderRequest) -> Unit//Lambda to call when locations are off.
    ) {
        val locationRequest = LocationRequest.Builder(//Create a location request object
            Priority.PRIORITY_HIGH_ACCURACY,//Self explanatory
            10000//Interval -> shorter the interval more frequent location updates
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())//Checksettings with building a request
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.d(
                "Location",
                "enableLocationRequest: LocationService Already Enabled"
            )
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()//Create the request prompt
                    makeRequest(intentSenderRequest)//Make the request from UI
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun registerLocationRequestLauncher() {
        locationRequestLauncher =//We will create a global var
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
                if (activityResult.resultCode == RESULT_OK)
                    locationViewModel.updateCurrentLocationData()//If the user clicks OK to turn on location
                else {
                    if (!locationViewModel.isLocationEnabled.value) {//If the user cancels, Still make a check and then exit the activity
                        Toast.makeText(
                            this,
                            "Location access is mandatory to use this feature!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        closeApp()
                    }
                }
            }
    }

    private fun closeApp() {
        val deviceId = (this.applicationContext as TrackApp).getDeviceID()
        locationViewModel.closeApp(deviceId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val stopIntent = Intent(this, ForegroundLocationService::class.java).setAction(
                ForegroundLocationService.ACTION_STOP_UPDATES
            )

            this.stopService(stopIntent)
        }
        finish()
    }

    private fun registerBroadcastReceiver() {
        val br = LocationProviderChangedReceiver()
        br.init(
            object : LocationProviderChangedReceiver.LocationListener {
                override fun onEnabled() {
                    locationViewModel.isLocationEnabled.value = true//Update our VM
                }

                override fun onDisabled() {
                    locationViewModel.isLocationEnabled.value = false//Update our VM
                }
            }
        )
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(br, filter)
    }
}
