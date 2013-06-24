package se.kjellstrand.colorclock.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import se.kjellstrand.colorclock.R;
import se.kjellstrand.colorclock.service.ClockService;

/**
 * Opens up the settings fragment.
 */
public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        mOnSharedPreferenceChangeListener.onSharedPreferenceChanged(sharedPreferences,
                getResources().getString(R.string.pref_charsets_key));
        mOnSharedPreferenceChangeListener.onSharedPreferenceChanged(sharedPreferences,
                getResources().getString(R.string.pref_blends_key));
        mOnSharedPreferenceChangeListener.onSharedPreferenceChanged(sharedPreferences,
                getResources().getString(R.string.pref_layouts_key));
    }

    /**
     * Listens for changes in SharedPreference, handles updates of the ui and
     * callback to the ClockService.
     */
    OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
        @SuppressLint("InlinedApi")
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Handle updates of the Charset setting.
            if (key.equals(getResources().getString(R.string.pref_charsets_key))) {
                @SuppressWarnings("deprecation")
                Preference charsetPref = findPreference(key);
                String charset = sharedPreferences.getString(key, getResources().getString(R.string.pref_charsets_default));
                String format = getResources().getString(R.string.pref_charsets_summary);
                // Set summary to be the user-description for the selected
                // value
                charsetPref.setSummary(String.format(format, charset));
            } else if (key.equals(getResources().getString(R.string.pref_blends_key))) {
                @SuppressWarnings("deprecation")
                Preference charsetPref = findPreference(key);
                String blend = sharedPreferences.getString(key, getResources().getString(R.string.pref_blends_default));
                String format = getResources().getString(R.string.pref_blends_summary);
                // Set summary to be the user-description for the selected
                // value
                charsetPref.setSummary(String.format(format, blend));
            } else if (key.equals(getResources().getString(R.string.pref_layouts_key))) {
                @SuppressWarnings("deprecation")
                Preference layoutPref = findPreference(key);
                String layout = sharedPreferences.getString(key, getResources().getString(R.string.pref_layouts_default));
                String format = getResources().getString(R.string.pref_layouts_summary);
                // Set summary to be the user-description for the selected
                // value
                layoutPref.setSummary(String.format(format, layout));
            }

            // Notify the service that at next update, it should re-read all its
            // settings.
            ClockService.settingsChanged();
        }
    };
}
