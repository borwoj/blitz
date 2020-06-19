package net.borysw.blitz.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import net.borysw.blitz.R

class GameTimeSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.game_time_settings, rootKey)
    }
}
