package uk.co.zac_h.spacex.feature.subscription

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.network.ApiTokenManager
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val apiTokenManager: ApiTokenManager
) : ViewModel() {

    val hasSubscribed: Boolean
        get() = apiTokenManager.hasSubscribed

    val plans: List<RecyclerViewItem>
        get() = SubscriptionPlans.plans(hasSubscribed)
            .groupBy { it.isCurrentPlan }
            .toSortedMap(compareBy { !it })
            .flatMap { (isCurrentPlan, planItem) ->
                listOf(
                    if (isCurrentPlan) {
                        CategoryItem("Current plan")
                    } else {
                        CategoryItem("Upgrade options")
                    }
                ) + planItem
            }

    fun subscribe() {
        apiTokenManager.hasSubscribed = true
    }

    fun unsubscribe() {
        apiTokenManager.hasSubscribed = false
    }
}