package com.sebastiancorradi.track.domain.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.services.ForegroundLocationService

class CreateNotificationChannelUseCase {
    operator fun invoke(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                ForegroundLocationService.NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }
}