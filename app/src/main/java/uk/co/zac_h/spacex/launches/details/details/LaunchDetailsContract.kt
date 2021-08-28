package uk.co.zac_h.spacex.launches.details.details

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Launch

interface LaunchDetailsContract {

    interface LaunchDetailsPresenter : NetworkInterface.Presenter<Launch?> {
        fun pinLaunch(id: String, pin: Boolean)
        fun isPinned(id: String?): Boolean
    }
}