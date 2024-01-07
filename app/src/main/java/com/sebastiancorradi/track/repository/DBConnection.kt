package com.sebastiancorradi.track.repository

import android.location.Location
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
    private val auth: FirebaseAuth by lazy { Firebase.auth}
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("borrar3")
    //private val authManager: AuthManager(context)
    fun testAddSomething(){
        //val myRef = database.getReference("message")
        //myRef.setValue("Hello, World!")
        val user = auth.currentUser
        Log.e("Sebastrack", "about to write, user: $user")
        val key = databaseReference.push().key
        if (key != null){
            databaseReference.child("ubicacion").setValue("hola carolassss")
        }
    }

    fun addLocation(location: Location, deviceId: String){

        addLocation(DBLocation(location.latitude, location.longitude, location.time, deviceId))
    }
    fun addLocation(dbLocation: DBLocation){
        Log.e("Sebastrack", "about to write, location: $dbLocation")
        val key = databaseReference.push().key
        if (key != null){
            databaseReference.child("locations").child(dbLocation.deviceId).child(dbLocation.date.toString()).setValue(dbLocation)
        }
    }

    fun getLocationFlow(userId: String): Flow<List<DBLocation>> {
        val flow = callbackFlow<List<DBLocation>> {
            val listener = databaseReference.addValueEventListener(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dbLocations = snapshot.children.mapNotNull {snapshot ->
                        val dbLocation = snapshot.getValue(DBLocation::class.java)
                        snapshot.key?.let { dbLocation?.copy() }
                    }
                    //TODO..... this
                   // trySend(dbLocations.filter { it.id == "id del usuario" })
                    trySend(dbLocations)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            awaitClose{databaseReference.removeEventListener(listener)}
        }

        return flow
    }
}