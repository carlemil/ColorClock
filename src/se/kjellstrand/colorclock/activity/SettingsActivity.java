package se.kjellstrand.colorclock.activity;

import se.kjellstrand.colorclock.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends Activity {

    /**
     * Tag for logging
     */
    private static final String TAG = SettingsActivity.class.getCanonicalName();

    /**
     * Constructor
     */
    public SettingsActivity() {
        Log.d(TAG, "SettingsActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SettingsActivity onCreate");
        
        setContentView(R.layout.settings);
        
    }
    
}
