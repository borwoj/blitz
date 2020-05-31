package net.borysw.blitz.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import net.borysw.blitz.R

class TimeSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.time_settings, rootKey)
    }
}
