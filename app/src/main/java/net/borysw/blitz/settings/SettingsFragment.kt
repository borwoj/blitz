package net.borysw.blitz.settings

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
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
        findPreference<Preference>("feedback")?.setOnPreferenceClickListener {
            launchEmail()
            true
        }
    }

    private fun launchEmail() {
        val recipient = getString(R.string.developer_email)
        val subject = "${getString(R.string.app_name)} - ${getString(R.string.feedback)}"
        val intent = Intent(ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipient?&subject=$subject")
        }
        startActivity(intent)
    }
}
