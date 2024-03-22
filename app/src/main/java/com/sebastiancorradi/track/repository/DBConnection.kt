package com.sebastiancorradi.track.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sebastiancorradi.track.TrackApp
import com.sebastiancorradi.track.data.DBLocation
import com.sebastiancorradi.track.data.LocationData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

@Singleton
class DBConnection() {
    private var locationCount: Int = 0
    private val TAG = "DBConnection"

    //private val auth: FirebaseAuth by lazy { Firebase.auth}
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("trackApp")

    init {
        val deviceId = TrackApp.getDeviceID()
        setLocationCountListener(deviceId)
    }

    fun addLocation(dbLocation: DBLocation) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child("locations").child(dbLocation.deviceId ?: "")
                .child(dbLocation.date.toString()).setValue(dbLocation)
        }
    }

    fun getLocationFlow(deviceId: String): Flow<List<LocationData>> {
        val flow = callbackFlow<List<LocationData>> {
            val listener = databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        val map = snapshot.getValue(true) as HashMap<String, *>
                        val list =
                            map.get("locations") as HashMap<String, HashMap<String, HashMap<String, String>>>
                        list.get(deviceId)?.let {
                            val deviceItem =
                                list.get(deviceId) as HashMap<String, HashMap<String, String>>
                            val deviceItemList = deviceItem.values.toList()
                            val locations = deviceItemList.map {
                                LocationData(deviceId, DBLocation(it))
                            }
                            trySend(locations.toList()
                                .sortedBy { (it.ubicacion?.date as Number).toLong() })
                        } ?: run {
                            Log.e(TAG, "locations retrived were null")
                            trySend(emptyList<LocationData>())
                        }
                    } catch (e: Exception) {
                        trySend(emptyList<LocationData>())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException()) // Cierra el flujo en caso de cancelación o error
                }

            })
            awaitClose { databaseReference.removeEventListener(listener) }
        }
        return flow
    }

    fun deleteLocations(deviceId: String) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child("locations").child(deviceId).root.setValue(null)
        }

    }

    fun setLocationCountListener(deviceId: String) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child("locations").child(deviceId).root.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val numChildren = (((dataSnapshot.children.toList()
                            .get(0).value as Map<Any, Any>).toList()
                            .get(0).second as Map<Any, Any>).toList().get(0).toList()
                            .get(1) as Map<Any, Any>).size
                        locationCount = numChildren.toInt()
                    } catch (e: Exception) {
                        locationCount = 0
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            });
        }
    }

    fun getLocationCount(): Int {
        return locationCount;
    }

}