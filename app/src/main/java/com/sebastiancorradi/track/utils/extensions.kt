package com.sebastiancorradi.track.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class extensions {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


}