package uk.co.zac_h.spacex.utils

import android.content.Context
import android.content.SharedPreferences

class OrderSharedPreferencesHelperImpl(private val preferences: SharedPreferences?) :
    OrderSharedPreferencesHelper {

    override fun setSortOrder(id: String, sortNew: Boolean) {
        preferences?.edit()?.putBoolean(id, sortNew)?.apply()
    }

    override fun isSortedNew(id: String): Boolean = preferences?.getBoolean(id, false) ?: false

    companion object Builder {

        fun build(context: Context?): OrderSharedPreferencesHelperImpl =
            OrderSharedPreferencesHelperImpl(
                context?.getSharedPreferences(
                    "order",
                    Context.MODE_PRIVATE
                )
            )

    }
}