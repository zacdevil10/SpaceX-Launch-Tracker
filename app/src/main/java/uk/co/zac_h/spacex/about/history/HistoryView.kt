package uk.co.zac_h.spacex.about.history

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel

interface HistoryView : NetworkInterface.View<ArrayList<HistoryHeaderModel>> {
    fun openWebLink(link: String)
}