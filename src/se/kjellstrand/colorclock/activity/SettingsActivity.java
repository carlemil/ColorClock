package se.kjellstrand.colorclock.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Opens up the settings fragment.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

}
