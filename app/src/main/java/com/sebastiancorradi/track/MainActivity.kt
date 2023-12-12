package com.sebastiancorradi.track

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sebastiancorradi.track.navigation.AppNavigation

class MainActivity: ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        if (auth.currentUser == null) {
            Log.e("Sebas", "no estaba logeado")
            setContent {
                AppNavigation()
            }
            // Not signed in, launch the Sign In activity
            //startActivity(Intent(this, SignInActivity::class.java))
            //finish()
            return
        } else {
            Log.e("Sebas", "SI estaba logeado")
            setContent {
                AppNavigation()
            }
        }
    }
}
