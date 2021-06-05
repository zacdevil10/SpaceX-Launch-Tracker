package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

interface HistoryContract {

    interface Presenter : NetworkInterface.Presenter<ArrayList<HistoryHeaderModel>?> {
        fun getOrder(): Boolean
        fun setOrder(order: Boolean)
    }

}