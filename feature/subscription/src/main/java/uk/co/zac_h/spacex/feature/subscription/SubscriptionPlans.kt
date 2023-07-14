package uk.co.zac_h.spacex.feature.subscription

import java.math.BigDecimal

object SubscriptionPlans {

    fun plans(hasSubscribed: Boolean): List<SubscriptionPlanItem> = listOfNotNull(
        if (!hasSubscribed) SubscriptionPlanItem(
            id = 0,
            name = "Basic",
            monthlyCost = BigDecimal("0.00"),
            renewalDate = null,
            isCurrentPlan = true,
            details = listOf(
                "15 API Calls per hour"
            )
        ) else null,
        SubscriptionPlanItem(
            id = 1,
            name = "Plus",
            monthlyCost = BigDecimal("2.99"),
            renewalDate = "11 August 2023",
            isCurrentPlan = hasSubscribed,
            details = listOf(
                "210 API Calls per hour",
                "Support development"
            )
        )
    )

}