package uk.co.zac_h.spacex.base.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import uk.co.zac_h.spacex.R

class NavigationWearAdapter(private val context: Context) :
    WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {

    override fun getCount(): Int = 4

    override fun getItemText(pos: Int): CharSequence =
        when (pos) {
            0 -> "Dashboard"
            1 -> "Upcoming Launches"
            2 -> "Past Launches"
            3 -> "About"
            else -> throw IllegalArgumentException("Drawer Position is out of range")
        }

    override fun getItemDrawable(pos: Int): Drawable? = when (pos) {
        0 -> ContextCompat.getDrawable(context, R.drawable.ic_dashboard_white_24dp)
        1 -> ContextCompat.getDrawable(context, R.drawable.ic_rocket)
        2 -> ContextCompat.getDrawable(context, R.drawable.ic_history_white_24dp)
        3 -> ContextCompat.getDrawable(context, R.drawable.ic_info_outline_white_24dp)
        else -> throw IllegalArgumentException("Drawer Position is out of range")
    }
}