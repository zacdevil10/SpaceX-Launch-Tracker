package uk.co.zac_h.spacex.feature.settings.theme

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.zac_h.spacex.core.common.DEFAULT_PREFERENCES
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePreferenceRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences =
        context.getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
    val themeMode: Int
        get() = sharedPreferences.getInt(
            PREFERENCES_NIGHT_MODE,
            PREFERENCES_NIGHT_MODE_DEF_VAL
        )

    private val themeModeLiveData: MutableLiveData<Int> = MutableLiveData()
    val themeModeLive: LiveData<Int> get() = themeModeLiveData

    var isTheme: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        get() = themeMode
        set(value) {
            sharedPreferences.edit().putInt(
                PREFERENCES_NIGHT_MODE, when (value) {
                    AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
                    AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                }
            ).apply()
            field = value
        }

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFERENCES_NIGHT_MODE -> {
                    themeModeLiveData.value = themeMode
                }
            }
        }

    init {
        themeModeLiveData.value = themeMode

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    companion object {
        private const val PREFERENCES_NIGHT_MODE = "preferences_night_mode"
        private val PREFERENCES_NIGHT_MODE_DEF_VAL =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else AppCompatDelegate.MODE_NIGHT_YES
    }
}
