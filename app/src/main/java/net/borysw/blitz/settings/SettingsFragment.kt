package net.borysw.blitz.settings

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.borysw.blitz.BuildConfig
import net.borysw.blitz.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val appVersion by lazy { BuildConfig.VERSION_NAME }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        findPreference<Preference>("game_time_settings")!!.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_timeSettingsFragment)
            true
        }
        findPreference<Preference>("game_type_settings")!!.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_typeSettingsFragment)
            true
        }
        findPreference<Preference>("feedback")!!.setOnPreferenceClickListener {
            launchEmail()
            true
        }
        findPreference<Preference>("version")!!.summary = appVersion
    }

    private fun launchEmail() {
        val recipient = getString(R.string.developer_email)
        val subject =
            "${getString(R.string.app_name)} $appVersion - ${getString(R.string.feedback)}"
        val intent = Intent(ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$recipient?&subject=$subject")
        }
        startActivity(intent)
    }
}
