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

public class ClockAppWidgetProvider extends AppWidgetProvider {

    /**
     * Tag for logging
     */
    private static final String TAG = ClockAppWidgetProvider.class
            .getName();

    private static final Intent mUpdateIntent = new Intent(
            ClockService.ACTION_UPDATE);
    
    private static final int ONE_SECOND = 1000;

    private static final int REQUEST_CODE = 810528;

    private Context mContext = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        this.mContext = context;
        this.mContext.startService(mUpdateIntent);
        createAlarm();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] remainingIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                this.getClass()));
        if (remainingIds == null || remainingIds.length <= 0) {
            PendingIntent pendingIntent = PendingIntent.getService(context, REQUEST_CODE,
                    mUpdateIntent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                AlarmManager alarmManager = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Log.d(TAG, "Alarm cancelled");
            }
        }
    }

    private void createAlarm() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.SECOND, 1);
        date.set(Calendar.MILLISECOND, 500);
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, REQUEST_CODE,
                mUpdateIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(mContext, REQUEST_CODE,
                    mUpdateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC, date.getTimeInMillis(), ONE_SECOND, pendingIntent);
            Log.d(TAG, "Alarm created.");
        }
    }
}
