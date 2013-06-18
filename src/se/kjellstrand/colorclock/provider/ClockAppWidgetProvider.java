package se.kjellstrand.colorclock.provider;

import java.util.Calendar;

import se.kjellstrand.colorclock.service.ClockService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Implements the clocks widget functionality.
 * 
 */
public class ClockAppWidgetProvider extends AppWidgetProvider {

    /**
     * Constant for 1 second, in milliseconds.
     */
    private static final int ONE_SECOND = 1000;

    /**
     * Code used to identify a request.
     */
    private static final int REQUEST_CODE = 810528;

    /**
     * Intent sent to the service to trigger a update of the clock ui.
     */
    private static final Intent UPDATE_INTENT = new Intent(ClockService.ACTION_UPDATE);

    /**
     * Tag for logging
     */
    private static final String TAG = ClockAppWidgetProvider.class.getName();

    /**
     * The Context in which this receiver is running.
     */
    private Context mContext = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("TAG,", "update");
        this.mContext = context;
        this.mContext.startService(UPDATE_INTENT);
        createAlarm();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] remainingIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));

        if (remainingIds == null || remainingIds.length <= 0) {
            PendingIntent pendingIntent = PendingIntent.getService(context, REQUEST_CODE, UPDATE_INTENT,
                    PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Log.d(TAG, "Alarm cancelled");
            }
        }
    }

    /**
     * Create a new alarm if none exists, or reuse the old.
     */
    private void createAlarm() {
        Log.d(TAG, "createAlarm.");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.SECOND, 1);
        date.set(Calendar.MILLISECOND, 500);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, REQUEST_CODE, UPDATE_INTENT,
                PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(mContext, REQUEST_CODE, UPDATE_INTENT,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC, date.getTimeInMillis(), ONE_SECOND, pendingIntent);
            Log.d(TAG, "Alarm created.");
        }
    }

}
