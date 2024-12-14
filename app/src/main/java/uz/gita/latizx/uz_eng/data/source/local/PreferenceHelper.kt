package uz.gita.latizx.uz_eng.data.source.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)

    companion object {
        private const val IS_FIRST_LAUNCH = "is_first_launch"
    }

    fun isFirstLaunch(): Boolean = pref.getBoolean(IS_FIRST_LAUNCH, true)
    fun setFirstLaunch(isFirst: Boolean) {
        pref.edit().putBoolean(IS_FIRST_LAUNCH, isFirst).apply()
    }
}