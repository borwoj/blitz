package net.borysw.blitz.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.borysw.blitz.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference<Preference>("time_settings")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_timeSettingsFragment)
            true
        }
        findPreference<Preference>("type_settings")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_typeSettingsFragment)
            true
        }
    }
}
