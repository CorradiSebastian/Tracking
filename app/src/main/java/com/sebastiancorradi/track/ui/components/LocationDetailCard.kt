package com.sebastiancorradi.track.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.EventType
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.utils.convertLongToTime

val TAG = "LocationDetailCard"

@Composable
fun LocationDetailCard(location: LocationData) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray, //Card background color
            //contentColor = Color.White  //Card content color,e.g.text
        )
    ) {
        Column(modifier =Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)) {


            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.Top,

                ) {
                    Text(
                        text = context.getString(R.string.date) + location.ubicacion?.date?.convertLongToTime(
                            context
                        ), style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                    Text(
                        text = context.getString(R.string.lat) + location.ubicacion?.lat,
                        style = TextStyle(
                            color = Color.Black, fontSize = 15.sp
                        )
                    )
                    Text(
                        text = context.getString(R.string.lon) + location.ubicacion?.long,
                        style = TextStyle(
                            color = Color.Black, fontSize = 15.sp
                        )
                    )
                }
                Spacer(Modifier.weight(1f))
                Column(modifier = Modifier.wrapContentSize()) {
                    Button(modifier = Modifier.wrapContentSize(), onClick = {
                        //_viewModel.locationsFlowRequested(deviceId)
                    }) {
                        Text(text = context.getString(R.string.details))
                    }
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = context.getString(R.string.event_type_label) + getEventType(
                        context, location
                    ),
                    style = TextStyle(
                        color = Color.Black, fontSize = 15.sp
                    )
                )
            }
        }
    }
}

fun getEventType(context: Context, location: LocationData): String{
    Log.e(TAG, "locaitonData: $location")
    when(location.ubicacion?.eventType){
        EventType.START -> {
            return context.getString(R.string.event_type_start)
        }
        EventType.STOP -> {
            return context.getString(R.string.event_type_stop)
        }
        else -> {
            return context.getString(R.string.event_type_track)
        }
    }
}
@Composable
fun DetailsContent() {
    val locations = remember { listOf(LocationData("user1", DBLocation(1.0, 2.0, 1705604793566L, "deviceID")),
                                      LocationData("user1", DBLocation(1.0, 2.0, 1705604598566L, "deviceID"))) }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            locations
        ) {
            LocationDetailCard(location = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    //LocationDetailCard(LocationData("user", DBLocation()))
    DetailsContent()
    /*TrackTheme {
        MainContent(MainScreenUIState())
    }*/
}