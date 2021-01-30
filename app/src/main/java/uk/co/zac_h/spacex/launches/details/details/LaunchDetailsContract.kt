package uk.co.zac_h.spacex.launches.details.details

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch

interface LaunchDetailsContract {

    interface LaunchDetailsView : NetworkInterface.View<Launch?> {
        fun newCalendarEvent()
        fun openWebLink(link: String)
    }

    interface LaunchDetailsPresenter : NetworkInterface.Presenter<Launch?> {
        fun addLaunchModel(launch: Launch?, isExt: Boolean)
        fun pinLaunch(id: String, pin: Boolean)
        fun isPinned(id: String): Boolean
        fun createEvent()
    }
}