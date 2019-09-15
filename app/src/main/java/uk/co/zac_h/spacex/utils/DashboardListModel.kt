package uk.co.zac_h.spacex.utils

import uk.co.zac_h.spacex.model.LaunchesModel

data class DashboardListModel(
    val launchModel: LaunchesModel?,
    val isHeader: Boolean = false,
    val headingName: String?
)