package uk.co.zac_h.spacex.launches.details

import android.view.View
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView

class LaunchDetailsPresenterImpl(private val view: LaunchDetailsView) : LaunchDetailsPresenter {

    override fun setExpandCollapseListener(
        view: View,
        recycler: RecyclerView,
        expCollapse: ToggleButton
    ) {
        view.setOnClickListener {
            this.view.expandCollapse(recycler, expCollapse)
        }
    }

    override fun expandCollapse(recycler: RecyclerView, expCollapse: ToggleButton) {
        view.setupExpandCollapse(recycler, expCollapse)
    }
}