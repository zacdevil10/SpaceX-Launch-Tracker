package uk.co.zac_h.spacex.launches.details

import android.view.View
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView

interface LaunchDetailsPresenter {

    fun setExpandCollapseListener(view: View, recycler: RecyclerView, expCollapse: ToggleButton)

    fun expandCollapse(recycler: RecyclerView, expCollapse: ToggleButton)
}