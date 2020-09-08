package uk.co.zac_h.spacex.utils.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import uk.co.zac_h.spacex.utils.widgets.services.LaunchWidgetService

class LaunchCountdownWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val intent = Intent(context.applicationContext, LaunchWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }

        context.startService(intent)
    }
}