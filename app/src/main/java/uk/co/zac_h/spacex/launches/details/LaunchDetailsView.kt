package uk.co.zac_h.spacex.launches.details

import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView

interface LaunchDetailsView {

    fun expandCollapse(recycler: RecyclerView, expCollapse: ToggleButton)

    fun setupExpandCollapse(recycler: RecyclerView, expCollapse: ToggleButton)
}