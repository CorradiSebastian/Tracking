package com.sebastiancorradi.track.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.sebastiancorradi.track.data.DBLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

@Singleton
class DBConnection() {
    private val TAG = "DBConnection"
    private val auth: FirebaseAuth by lazy { Firebase.auth}
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("trackApp")

    fun addLocation(dbLocation: DBLocation){
        Log.e(TAG, "addLocation, about to write, location: $dbLocation")
        val key = databaseReference.push().key
        if (key != null){
            databaseReference.child("locations").child(dbLocation.deviceId?:"").child(dbLocation.date.toString()).setValue(dbLocation)
        }
    }

    fun getLocationFlow(deviceId: String): Flow<List<DBLocation>> {
        Log.e(TAG, "getLocationFLow, deviceId: $deviceId")
        val flow = callbackFlow<List<DBLocation>> {
            val listener = databaseReference.addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        val map = snapshot.getValue(true) as HashMap<*, *>
                        val list = map.get("locations") as HashMap<*, *>
                        val deviceItem = list.get(deviceId) as HashMap<String, DBLocation>
                        val deviceItemList = deviceItem.values

                        trySend(deviceItemList.toList())
                    } catch (e: Exception){
                        Log.e(TAG,  "error, e: $e")
                        trySend(emptyList<DBLocation>())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException()) // Cierra el flujo en caso de cancelación o error
                }

            })
            awaitClose{databaseReference.removeEventListener(listener)}
        }
        return flow
    }

}