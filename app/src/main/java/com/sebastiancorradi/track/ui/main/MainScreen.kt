package com.sebastiancorradi.track.ui.main

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.sebastiancorradi.track.MainActivity
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.navigation.AppScreens
import com.sebastiancorradi.track.ui.components.LoadingButton
import com.sebastiancorradi.track.ui.components.LocationDetailCard
import com.sebastiancorradi.track.ui.components.RequestPermissions2
import com.sebastiancorradi.track.ui.theme.TrackTheme

val TAG = "MainScreen"
var _navController: NavController? = null
@Composable
fun MainScreen(navController: NavController?, mainViewModel: MainViewModel = viewModel()){
    _navController = navController
    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()
    Log.e(TAG, "main screen initialization")
    //mainViewModel = _mainViewModel
    TrackTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainContent(mainScreenUIState, mainViewModel)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(mainScreenUIState: MainScreenUIState, mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Log.e(TAG, "main screen maincontent")
    Surface(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        //RequestPermissions2()
        Column(modifier = Modifier
            .fillMaxWidth(),

            ) {
            Text(
                stringResource(id = R.string.welcome),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,

                modifier = Modifier.fillMaxWidth(),
                )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                stringResource(id = R.string.welcome_text_1),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            ConceptsList()
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(id = R.string.welcome_text_2),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(id = R.string.welcome_text_3),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ){
                /*LoadingButton(color = colorResource(R.color.buttonEnabled), text = "start") {
                    
                }*/
                ElevatedButton(
                    modifier = Modifier.background(colorResource(R.color.buttonEnabled)),
                    onClick = { Log.e(TAG, "clicked") },
                    //onClick = { mainViewModel.startTrackingClicked() },
                    //onClick = { _navController!!.navigate(AppScreens.LocationScreen.route) },
                    //colors = ButtonColors(containerColor = colorResource(R.color.buttonEnabled)),
                ) {
                    Text("Resume")
                }
                ElevatedButton(
                    modifier = Modifier.background(colorResource(R.color.buttonEnabled)),
                    //onClick = { mainViewModel.startTrackingClicked() },
                    onClick = { signIn(context) },
                    //colors = ButtonColors(containerColor = colorResource(R.color.buttonEnabled)),
                ) {
                    Text("Login")
                }
            }

        }
    }
}

@Composable
fun ConceptsList() {
    val myList = stringArrayResource(id = R.array.concepts).toList()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(myList) {
            Text(it,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth())
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

//@Composable
fun signIn(context: Context) {// Create and launch sign-in intent
    //val context = LocalContext.current
    (context as MainActivity).signIn()
}

/*

private val signInLauncher = registerForActivityResult(
    FirebaseAuthUIActivityResultContract(),
) { res ->
    onSignInResult(res)
}

private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
    Log.e(TAG, "onSigninResult: $result")
    val response = result?.idpResponse
    if (result?.resultCode == ComponentActivity.RESULT_OK) {
        // Successfully signed in
        val user = FirebaseAuth.getInstance().currentUser
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
*/

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(null)
    /*TrackTheme {
        MainContent(MainScreenUIState())
    }*/
}
