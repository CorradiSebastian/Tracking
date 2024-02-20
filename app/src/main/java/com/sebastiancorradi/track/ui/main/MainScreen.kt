package com.sebastiancorradi.track.ui.main

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sebastiancorradi.track.MainActivity
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.ui.theme.TrackTheme

val TAG = "MainScreen"
@Composable
fun MainScreen(onResumeClicked: () -> Unit,
               mainViewModel: MainViewModel = viewModel(),){

    val mainScreenUIState by mainViewModel.mainScreenUIState.collectAsState()
    if (mainScreenUIState.resumeClicked){
        onResumeClicked
    } else {
        TrackTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                MainContent(mainScreenUIState, mainViewModel)
            }
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
                ElevatedButton(
                    modifier = Modifier.background(colorResource(R.color.buttonEnabled)),
                    //onClick = { Log.e(TAG, "clicked") },
                    onClick = { mainViewModel.resumeClicked()},
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

//@Composable
fun signIn(context: Context) {// Create and launch sign-in intent
    //val context = LocalContext.current
    (context as MainActivity).signIn()
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen( {})
    /*TrackTheme {
        MainContent(MainScreenUIState())
    }*/
}
