package com.sebastiancorradi.track

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sebastiancorradi.track.navigation.AppNavigation
import com.sebastiancorradi.track.ui.location.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val locationViewModel: LocationViewModel by viewModels()
    //private val locationViewModel: LocationViewModel by hiltNavGraphViewModels(R.id.my_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        if (auth.currentUser == null) {
            setContent {
                AppNavigation()
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
}
