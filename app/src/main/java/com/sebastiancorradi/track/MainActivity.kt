package com.sebastiancorradi.track

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sebastiancorradi.track.navigation.AppNavigation
import com.sebastiancorradi.track.ui.location.LocationViewModel
import com.sebastiancorradi.track.ui.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
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
        Log.e(TAG, "onresume ONRESUME onresume")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        Log.e(TAG, "oncreate, auth: $auth")
        Log.e(TAG, "oncreate, auth.currentUser: ${auth.currentUser}")
        if (auth.currentUser == null) {
            //LAUNCH THE LOGIN SCREEN
            setContent {
                //_navController!!.navigate(AppScreens.LocationScreen.route)
                MainScreen(navController = null)
                //AppNavigation()
            }
            // Not signed in, launch the Sign In activity
            //startActivity(Intent(this, SignInActivity::class.java))
            //finish()
            return
        } else {
            setContent {
                AppNavigation()
            }
        }
    }

    // Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        //AuthUI.IdpConfig.FacebookBuilder().build(),
        //AuthUI.IdpConfig.TwitterBuilder().build(),
    )

    fun signIn() {// Create and launch sign-in intent
        val signInIntent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build()
        Log.e(TAG, "before signInLauncher.launch")
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        Log.e(TAG, "onSigninResult: $result")
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            vie
            auth.currentUser
            // ...
        } else {
            Log.e(TAG, "onSigninResult FAILED: $result")
            Log.e(TAG, "onSigninResult FAILED: ${response?.getError()?.getErrorCode()}")
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }


}
