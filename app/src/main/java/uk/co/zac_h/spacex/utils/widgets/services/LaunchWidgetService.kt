package uk.co.zac_h.spacex.utils.widgets.services

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.util.concurrent.TimeUnit

class LaunchWidgetService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appWidgetManager = AppWidgetManager.getInstance(this.applicationContext)

        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        allWidgetIds?.forEach {
            val remoteView =
                RemoteViews(this.applicationContext.packageName, R.layout.launch_countdown_widget)

            CoroutineScope(Dispatchers.Main).launch {
                val response = SpaceXInterface.create().getSingleLaunch("87")

                try {
                    if (response.isSuccessful) {
                        remoteView.setTextViewText(
                            R.id.launch_countdown_widget_text,
                            response.body()?.tbd?.let { tbd ->
                                if (!tbd) {
                                    response.body()?.launchDateUnix?.let { it1 ->
                                        updateCountdown(
                                            it1.times(1000) - System.currentTimeMillis()
                                        )
                                    } ?: "TBD"
                                } else {
                                    "TBD"
                                }
                            } ?: "TBD")

                        remoteView.setTextViewText(
                            R.id.launch_countdown_widget_mission_text,
                            response.body()?.missionName
                        )

                        appWidgetManager.updateAppWidget(it, remoteView)
                    }
                } catch (e: HttpException) {
                    Log.e(
                        this@LaunchWidgetService.javaClass.name,
                        e.localizedMessage ?: "HTTP Exception"
                    )
                } catch (e: Throwable) {
                    Log.e(
                        this@LaunchWidgetService.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }

        stopSelf()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun updateCountdown(time: Long): String = String.format(
        "T-%02dD:%02dH",
        TimeUnit.MILLISECONDS.toDays(time),
        TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(
            TimeUnit.MILLISECONDS.toDays(time)
        ),
        TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(time)
        ),
        TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(time)
        )
    )
}
