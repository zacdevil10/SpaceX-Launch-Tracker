package uk.co.zac_h.spacex.utils.widgets.services

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import java.util.concurrent.TimeUnit

class LaunchWidgetService : Service(), LaunchWidgetContract.Service {

    private var appWidgetManager: AppWidgetManager? = null

    private var presenter: LaunchWidgetContract.Presenter? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        appWidgetManager = AppWidgetManager.getInstance(this.applicationContext)

        presenter = LaunchWidgetPresenter(this, LaunchWidgetInteractor())

        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        allWidgetIds?.forEach {
            presenter?.getLaunch(it)
        }

        stopSelf()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun updateWidget(id: Int, launchModel: LaunchesExtendedModel) {
        val remoteView =
            RemoteViews(this.applicationContext.packageName, R.layout.launch_countdown_widget)

        remoteView.setTextViewText(
            R.id.launch_countdown_widget_text,
            launchModel.tbd?.let { tbd ->
                if (!tbd) {
                    launchModel.launchDateUnix?.let { it1 ->
                        val time = it1.times(1000) - System.currentTimeMillis()
                        if (time >= 0) {
                            updateCountdown(time)
                        } else {
                            "Launching"
                        }
                    } ?: "TBD"
                } else {
                    "TBD"
                }
            } ?: "TBD")

        remoteView.setTextViewText(
            R.id.launch_countdown_widget_mission_text,
            launchModel.missionName
        )

        appWidgetManager?.updateAppWidget(id, remoteView)
    }

    override fun showError(error: String) {

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
