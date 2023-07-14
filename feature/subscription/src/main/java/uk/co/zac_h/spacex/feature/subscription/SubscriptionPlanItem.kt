package uk.co.zac_h.spacex.feature.subscription

import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import java.math.BigDecimal

data class SubscriptionPlanItem(
    val id: Int,
    val name: String,
    val monthlyCost: BigDecimal,
    val renewalDate: String?,
    val isCurrentPlan: Boolean,
    val details: List<String>
) : RecyclerViewItem