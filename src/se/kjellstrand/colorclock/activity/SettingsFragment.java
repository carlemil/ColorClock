package se.kjellstrand.colorclock.activity;

import se.kjellstrand.colorclock.R;
import se.kjellstrand.colorclock.service.ClockService;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        prefs.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    /**
     * 
     */
    OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d("TAG", "onSharedPreferenceChanged " + key);
            String prefCharsetForDigitsKey = getActivity().getResources().getString(R.string.pref_charsets_for_digits);
            if (key.equals(prefCharsetForDigitsKey)) {
                Preference charsetPref = findPreference(key);
                String charset = sharedPreferences.getString(key,
                        getResources().getString(R.string.charset_for_digits_latin));
                String format = getResources().getString(R.string.charsets_for_digits_summary);
                // Set summary to be the user-description for the selected value
                charsetPref.setSummary(String.format(format, charset));
            }

            // Notify the service that at next update, it should re-read all its
            // settings.
            ClockService.settingsChanged();
        }
    };

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.d("TAG", "resume");
    }

}
