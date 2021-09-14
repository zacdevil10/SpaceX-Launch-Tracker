package uk.co.zac_h.spacex.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class LocalSharedPreferences<R>(
    context: Context,
    preferencesType: String
) {

    protected val preferences: SharedPreferences =
        context.getSharedPreferences(preferencesType, Context.MODE_PRIVATE)

    private val _allLiveData: MutableLiveData<MutableMap<String, *>> = MutableLiveData(getAll())
    val allLiveData: LiveData<MutableMap<String, *>> = _allLiveData

    private val preferencesChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            _allLiveData.value = getAll()
        }

    init {
        preferences.registerOnSharedPreferenceChangeListener(preferencesChangedListener)
    }

    private fun getAll(): MutableMap<String, *>? = preferences.all

    abstract fun getValue(key: String, default: R): R

    abstract fun add(key: String, value: R)

    abstract fun remove(key: String)
}