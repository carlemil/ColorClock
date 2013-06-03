package se.kjellstrand.colorclock.activity;

import se.kjellstrand.colorclock.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.widget.Toast;

/**
 * Opens up the info fragment.
 */
public class ShowAlarmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
            startActivity(i);
        } else {
            Toast.makeText(getBaseContext(), R.string.pref_alarm_to_low_api, Toast.LENGTH_LONG).show();
        }

        finish();

    }
}
