package uk.co.zac_h.spacex.base.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import uk.co.zac_h.spacex.R

class NavigationWearAdapter(private val context: Context) :
    WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {

    override fun getCount(): Int = 4

    override fun getItemText(pos: Int): CharSequence =
        when (pos) {
            0 -> "Dashboard"
            1 -> "Launches"
            2 -> "Statistics"
            3 -> "About"
            else -> throw IllegalArgumentException("Drawer Position is out of range")
        }

    override fun getItemDrawable(pos: Int): Drawable? = when (pos) {
        0 -> context.getDrawable(R.drawable.ic_dashboard_white_24dp)
        1 -> context.getDrawable(R.drawable.ic_build_white_24dp)
        2 -> context.getDrawable(R.drawable.ic_build_white_24dp)
        3 -> context.getDrawable(R.drawable.ic_build_white_24dp)
        else -> throw IllegalArgumentException("Drawer Position is out of range")
    }
}