package com.sebastiancorradi.track.services



import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.sebastiancorradi.track.R
import com.sebastiancorradi.track.domain.StartTrackingUseCase
import com.sebastiancorradi.track.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Service which manages turning location updates on and off. UI clients should bind to this service
 * to access this functionality.
 *
 * This service can be started the usual way (i.e. startService), but it will also start itself when
 * the first client binds to it. Thereafter it will manage its own lifetime as follows:
 *   - While there are any bound clients, the service remains started in the background. If it was
 *     in the foreground, it will exit the foreground, cancelling any ongoing notification.
 *   - When there are no bound clients and location updates are on, the service moves to the
 *     foreground and shows an ongoing notification with the latest location.
 *   - When there are no bound clients and location updates are off, the service stops itself.
 */
@AndroidEntryPoint
class ForegroundLocationService : LifecycleService() {
//class ForegroundLocationService() : Service(), LifecycleOwner {

    //@Inject
    //lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var startTrackingUseCase: StartTrackingUseCase
    @Inject
    lateinit var stopTrackingUseCase: StartTrackingUseCase
/*
    @Inject
    lateinit var locationPreferences: LocationPreferences*/

    private val localBinder = LocalBinder()
    private var bindCount = 0

    private var started = false
    private var isForeground = false

    private fun isBound() = bindCount > 0

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        //TODO ver que pasa si llamo a startForeground una vez que ya tengo el location
        startForeground(NOTIFICATION_ID, buildNotification(null))

    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e("Sebastrack", "onStartCommand")
        if (ACTION_STOP_UPDATES.equals(intent?.getAction())) {
            stopSelf();
            //locationRepository.stopLocationUpdates()
            stopTrackingUseCase()
            return START_NOT_STICKY
        }
        val notification = buildNotification(null)
        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ 100, // Cannot be 0
            /* notification = */ notification,
            /* foregroundServiceType = */
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
        )
        // This action comes from our ongoing notification. The user requested to stop updates.
        /*if (intent?.action == ACTION_STOP_UPDATES) {
            stopLocationUpdates()
            lifecycleScope.launch {
                locationPreferences.setLocationTurnedOn(false)
            }
        }*/

        // Startup tasks only happen once.
        if (!started) {
            started = true
            // Check if we should turn on location updates.
            lifecycleScope.launch {
                /*if (locationPreferences.isLocationTurnedOn.first()) {
                    // If the service is restarted for any reason, we may have lost permission to
                    // access location since last time. In that case we won't turn updates on here,
                    // and the service will stop when we manage its lifetime below. Then the user
                    // will have to open the app to turn updates on again.
                    if (hasPermission(permission.ACCESS_FINE_LOCATION) ||
                        hasPermission(permission.ACCESS_COARSE_LOCATION)
                    ) {
                        locationRepository.startLocationUpdates()
                    }
                }*/
                Log.e("Sebastrack", "about to call usecase, starting location updates")
                startTrackingUseCase()
            }
            // Update any foreground notification when we receive location updates.
            /*lifecycleScope.launch {
                locationRepository.lastLocation.collect(::showNotification)
            }*/
        }

        // Decide whether to remain in the background, promote to the foreground, or stop.

        // In case we are stopped by the system, have the system restart this service so we can
        // manage our lifetime appropriately.
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        handleBind()
        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        handleBind()
    }

    private fun handleBind() {
        bindCount++
        // Start ourself. This will let us manage our lifetime separately from bound clients.
        startService(Intent(this, this::class.java))
    }

    override fun onUnbind(intent: Intent?): Boolean {
        bindCount--
        lifecycleScope.launch {
            // UI client can unbind because it went through a configuration change, in which case it
            // will be recreated and bind again shortly. Wait a few seconds, and if still not bound,
            // manage our lifetime accordingly.
            delay(UNBIND_DELAY_MILLIS)
            //manageLifetime()
        }
        // Allow clients to rebind, in which case onRebind will be called.
        return true
    }

    private fun showNotification(location: Location?) {
        if (!isForeground) {
            return
        }

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification(location))
    }

    private fun createNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }

    private fun buildNotification(location: Location?) : Notification {
        // Tapping the notification opens the app.
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            packageManager.getLaunchIntentForPackage(this.packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Include an action to stop location updates without going through the app UI.
        val stopIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, this::class.java).setAction(ACTION_STOP_UPDATES),
            FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentText = if (location != null) {
            //getString(R.string.location_lat_lng, location.latitude, location.longitude)
            "valor"
        } else {
            //getString(R.string.waiting_for_location)
            "Esperando"
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            //TODO create icons
            //.addAction(R.drawable.ic_stop, getString(R.string.stop), stopIntent)
            .addAction(R.drawable.ic_launcher_foreground, "stop", stopIntent)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .build()
    }

    // Methods for clients.

    fun startLocationUpdates() {
        startTrackingUseCase()
    }

    fun stopLocationUpdates() {
        stopTrackingUseCase()
    }

    /** Binder which provides clients access to the service. */
    internal inner class LocalBinder : Binder() {
        fun getService(): ForegroundLocationService = this@ForegroundLocationService
    }

    private companion object {
        const val UNBIND_DELAY_MILLIS = 2000.toLong() // 2 seconds
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "LocationUpdates"
        const val ACTION_STOP_UPDATES = ".ACTION_STOP_UPDATES"
    }

}

/**
 * ServiceConnection that provides access to a [ForegroundLocationService].
 */

class ForegroundLocationServiceConnection @Inject constructor() : ServiceConnection {

    var service: ForegroundLocationService? = null
        private set

    override fun onServiceConnected(name: ComponentName, binder: IBinder) {
        service = (binder as ForegroundLocationService.LocalBinder).getService()
    }

    override fun onServiceDisconnected(name: ComponentName) {
        // Note: this should never be called since the service is in the same process.
        service = null
    }
}
