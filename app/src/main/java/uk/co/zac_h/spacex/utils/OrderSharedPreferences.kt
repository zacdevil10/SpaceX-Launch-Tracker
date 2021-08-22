package uk.co.zac_h.spacex.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderSharedPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val preferences = context.getSharedPreferences("order", Context.MODE_PRIVATE)

    fun setSortOrder(id: String, sortNew: Boolean) {
        preferences?.edit()?.putBoolean(id, sortNew)?.apply()
    }

    fun isSortedNew(id: String): Boolean = preferences?.getBoolean(id, false) ?: false

    companion object Builder {

        fun build(context: Context): OrderSharedPreferences =
            OrderSharedPreferences(context)

    }
}