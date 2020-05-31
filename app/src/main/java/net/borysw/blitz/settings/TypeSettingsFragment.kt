package net.borysw.blitz.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import net.borysw.blitz.R

class TypeSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.type_settings, rootKey)
    }
}
