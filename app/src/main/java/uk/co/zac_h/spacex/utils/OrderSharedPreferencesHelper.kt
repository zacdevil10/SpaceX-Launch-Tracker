package uk.co.zac_h.spacex.utils

interface OrderSharedPreferencesHelper {

    fun setSortOrder(id: String, sortNew: Boolean)

    fun isSortedNew(id: String): Boolean

}