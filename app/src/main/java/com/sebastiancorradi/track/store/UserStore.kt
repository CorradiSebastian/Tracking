package com.sebastiancorradi.track.store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")
        //private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name ="userStore")
        private val TRACKING_KEY = booleanPreferencesKey("tracking")
    }

    fun getDataStore():DataStore<Preferences>{
        return context.dataStore
    }

    val getTrackingStatus: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TRACKING_KEY] ?: false
    }

    suspend fun saveTrackingStatus(tracking: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TRACKING_KEY] = tracking
        }
    }
}