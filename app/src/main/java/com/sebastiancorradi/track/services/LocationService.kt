package com.sebastiancorradi.track.services

import android.annotation.SuppressLint
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.sebastiancorradi.track.ui.components.checkLocationGranted
import com.sebastiancorradi.track.ui.components.subscribeToLocationUpdates
import com.sebastiancorradi.track.ui.location.locationUpdate

class LocationService: Service() {
    override fun onCreate() {
        super.onCreate()
        Log.e("Sebastito", "service oncreate")
    }

    @SuppressLint("NewApi")
    private fun startForeground() {
        // Before starting the service as foreground check that the app has the
        // appropriate runtime permissions. In this case, verify that the user has
        // granted the CAMERA permission.
        //TODO this permission is already been checked on LocationScreen, as a service have no visual component, I guess this should be checked prior to start the service
        val foregroundLocationPermision =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        if (foregroundLocationPermision == PackageManager.PERMISSION_DENIED) {
            // Without camera permissions the service cannot run in the foreground
            // Consider informing user or updating your app UI if visible.
            stopSelf()
            return
        }

        try {
            val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
                // Create the notification to display while the service is running
                .build()
            val notification2 = createNotificationChanel(context = this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(1, notification2)
                //startForegroundService()
            }
            /*ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ 100, // Cannot be 0
                /* notification = */ notification,
                /* foregroundServiceType = */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                } else {
                    0
                },
            )*/
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
            // ...
        }
    }

    override fun onBind(p0: Intent?): IBinder? {

        Log.e("Sebastito", "service binded")
        return null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel(context: Context): Notification {
        val NOTIFICATION_CHANNEL_ID = "com.getlocationbackground"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.Blue.toArgb()
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            //.setContentTitle("App is running count::" + counter)
            .setContentTitle("service is running")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        //val intent = Intent(context, LocationService::class.java)
        //context.startForegroundService(intent)
        return notification
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("Sebastito", "onStarCommand service 0")
        if (intent == null) {
            Log.e("Sebastito", "onStarCommand service intent era nul")
            return START_STICKY_COMPATIBILITY
        } else {
            Log.e("Sebastito", "about to call StartForeground, inside Service")
            startForeground()
            performLongTask()
            Log.e("Sebastito", "onStarCommand service 1")
            //subscribe()
        }
        Log.e("Sebastito", "onStarCommand service 2")
        return START_STICKY
    }

    private fun subscribe(){
        subscribeToLocationUpdates(this, ::locationUpdate)
    }

    fun locationUpdate(location: Location){
        //TODO develop what to do on updates
        Log.e(
            "sebastrack",
            "updated lat: ${location.latitude}, long: ${location.longitude}"
        )
    }
    private fun permissionDenied() {
        Log.e(
            "sebastito",
            "denied"
        )
    }
    override fun onDestroy() {
        Log.e("Sebastito", "ondestroy")
        super.onDestroy()
    }
    private fun performLongTask() {
        // Imagine doing something that takes a long time here
        Log.e("Sebastito", "performLong Start")
        Thread.sleep(5000)
        Log.e("Sebastito", "performLong End")
    }
}