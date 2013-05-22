package se.kjellstrand.colorclock.activity;

import se.kjellstrand.colorclock.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * Opens up the info fragment.
 */
public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info);

    }
}
