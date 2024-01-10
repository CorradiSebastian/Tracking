package com.sebastiancorradi.track.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.LocationData
import com.sebastiancorradi.track.ui.main.MainScreen

@Composable
fun LocationDetailCard(location: LocationData) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray, //Card background color
            //contentColor = Color.White  //Card content color,e.g.text
        )
    ) {

        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f),
                Arrangement.Center) {
                Text(
                    text = "date: " + location.ubicacion?.date,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    text = "lat: "+location.ubicacion?.lat,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "lon: "+location.ubicacion?.long,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )


            }
            Button(onClick = {
                //_viewModel.locationsFlowRequested(deviceId)
            }) {
                Text(text = "map")
            }

        }
    }
}

@Composable
fun DetailsContent() {
    val locations = remember { listOf(LocationData("user1", DBLocation(1.0, 2.0, 234234L, "deviceID")),
                                      LocationData("user1", DBLocation(1.0, 2.0, 234234L, "deviceID"))) }
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