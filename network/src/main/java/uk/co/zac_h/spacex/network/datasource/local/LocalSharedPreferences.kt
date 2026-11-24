package uk.co.zac_h.spacex.network.datasource.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class LocalSharedPreferences<R>(
    context: Context,
    preferencesType: String
) {

    protected val preferences: SharedPreferences =
        context.getSharedPreferences(preferencesType, Context.MODE_PRIVATE)

    private val _all: MutableStateFlow<Map<String, *>?> = MutableStateFlow(getAll())
    val all: StateFlow<Map<String, *>?> = _all.asStateFlow()

    private val preferencesChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            _all.value = getAll()
        }

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferencesChangedListener)
    }

    private fun getAll(): Map<String, *>? = preferences.all

    abstract fun getValue(key: String, default: R): R

    abstract fun add(key: String, value: R)

    abstract fun remove(key: String)
}
