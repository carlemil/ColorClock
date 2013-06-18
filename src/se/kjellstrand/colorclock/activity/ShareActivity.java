package se.kjellstrand.colorclock.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import se.kjellstrand.colorclock.R;

/**
 * Opens up the native share dialog.
 */
public class ShareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.sharing_text));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        finish();
    }
}
