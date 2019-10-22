package uk.co.zac_h.spacex.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import uk.co.zac_h.spacex.R

object NextLaunchNotification {

    private const val NOTIFICATION_TAG = "NextLaunch"
    private const val NOTIFICATION_CHANNEL_ID = "next_launch_channel"

    fun notify(context: Context, message: String?, number: Int) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_spacex_icon)
            setContentTitle("SpaceX Launch")
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setNumber(number)
            setTicker(message)
            setAutoCancel(true)
        }

        notify(context, builder.build())
    }

    private fun notify(context: Context, notification: Notification) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Next Launch Countdown",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(notificationChannel)
        }
        nm.notify(NOTIFICATION_TAG, 0, notification)
    }

    fun cancel(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_TAG, 0)
    }
}