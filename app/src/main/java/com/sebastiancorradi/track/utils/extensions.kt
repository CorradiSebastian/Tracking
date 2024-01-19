package com.sebastiancorradi.track.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.sebastiancorradi.track.R
import java.text.SimpleDateFormat
import java.util.Date

class extensions {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")



}

fun Long.convertLongToTime(context: Context): String {
    val date = Date(this)
    val localFormat = context.resources.getString(R.string.date_time_format)
    val format = SimpleDateFormat(localFormat)
    return format.format(date)
}