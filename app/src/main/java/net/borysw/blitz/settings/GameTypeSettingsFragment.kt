package net.borysw.blitz.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.android.support.AndroidSupportInjection.inject
import net.borysw.blitz.R
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_BRONSTEIN_DELAY
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_FISCHER
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_SIMPLE_DELAY
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_STANDARD
import net.borysw.blitz.settings.PreferencesConstants.KEY_DELAY
import net.borysw.blitz.settings.PreferencesConstants.KEY_GAME_TYPE
import net.borysw.blitz.settings.PreferencesConstants.KEY_INCREMENT_BY
import javax.inject.Inject

class GameTypeSettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefs: RxSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.game_type_settings, rootKey)

        findPreference<Preference>(KEY_GAME_TYPE)!!.setOnPreferenceChangeListener { _, gameType ->
            togglePreferences(gameType)
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        togglePreferences(prefs.getString(KEY_GAME_TYPE).get())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun togglePreferences(newValue: Any?) {
        val delayPref = findPreference<Preference>(KEY_DELAY)!!
        val incrementPref = findPreference<Preference>(KEY_INCREMENT_BY)!!

        when (newValue) {
            GAME_TYPE_STANDARD -> {
                delayPref.disable()
                incrementPref.disable()
            }
            GAME_TYPE_SIMPLE_DELAY -> {
                delayPref.enable()
                incrementPref.disable()
            }
            GAME_TYPE_BRONSTEIN_DELAY -> {
                delayPref.enable()
                incrementPref.disable()
            }
            GAME_TYPE_FISCHER -> {
                delayPref.disable()
                incrementPref.enable()
            }
        }
    }

    private fun Preference.disable() {
        isEnabled = false
    }

    private fun Preference.enable() {
        isEnabled = true
    }
}
