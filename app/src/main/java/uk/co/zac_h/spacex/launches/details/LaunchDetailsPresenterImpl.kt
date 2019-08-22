package uk.co.zac_h.spacex.launches.details

import android.view.View

class LaunchDetailsPresenterImpl(private val view: LaunchDetailsView) : LaunchDetailsPresenter {

    override fun setExpandCollapseListener(view: View, recycler: View, expCollapseIcon: View) {
        view.setOnClickListener {
            this.view.expandCollapse(recycler, expCollapseIcon)
        }
    }
}