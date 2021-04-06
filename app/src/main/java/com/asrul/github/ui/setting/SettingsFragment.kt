package com.asrul.github.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.asrul.github.DailyReminder
import com.asrul.github.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminderSwitch: SwitchPreferenceCompat
    private lateinit var dailyReminder: DailyReminder
    private lateinit var reminder: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = resources.getString(R.string.setting)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        dailyReminder = DailyReminder()

        initReminder()
        initSharedPreference()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun initSharedPreference() {
        val sharedPreferences = preferenceManager.sharedPreferences
        reminderSwitch.isChecked = sharedPreferences.getBoolean(reminder, true)
    }

    private fun initReminder() {
        reminder = resources.getString(R.string.reminder)
        reminderSwitch = findPreference<SwitchPreferenceCompat>(reminder) as SwitchPreferenceCompat
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == reminder) {
            if (sharedPreferences != null) {
                reminderSwitch.isChecked = sharedPreferences.getBoolean(reminder, false)
            }
        }

        val state: Boolean = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(reminder, false)
        setReminder(state)
    }

    private fun setReminder(state: Boolean) {
        if (state) {
            context?.let { dailyReminder.setRepeatReminder(it) }
        } else {
            context?.let { dailyReminder.cancelAlarm(it) }
        }
    }
}