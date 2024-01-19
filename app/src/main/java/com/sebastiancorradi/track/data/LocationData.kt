package com.sebastiancorradi.track.data

import android.util.Log
import androidx.compose.ui.text.toUpperCase

data class LocationData(
    val user: String? = null,
    val ubicacion: DBLocation? = null,
    )

enum class EventType {
    START, STOP, TRACK
}

fun getEventType(value:String):EventType{
    when (value.uppercase()){
        EventType.START.toString() ->{
            return EventType.START
        }
        EventType.STOP.toString() ->{
            return EventType.STOP
        }
        else ->{
            return EventType.TRACK
        }
    }
}
data class DBLocation(
    var lat: Double? = null,
    var long: Double? = null,
    var date: Long? = null,
    var deviceId:String? = null,
    var eventType: EventType?= null,
) {
    constructor(dbHashMap: HashMap<String, String>) : this(
        lat = (dbHashMap.getValue("lat") as Number).toDouble(),
        long = (dbHashMap.getValue("long") as Number).toDouble(),
        date = (dbHashMap.getValue("date") as Number).toLong(),
        deviceId = dbHashMap.getValue("deviceId"),
        eventType = getEventType(dbHashMap.getValue("eventType")),
    )
}

