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

public class ColorClockAppWidgetProvider extends AppWidgetProvider {

    /**
     * TAG for LogCat logging
     */
    private static final String TAG = ColorClockAppWidgetProvider.class
            .getName();

    private static final Intent mUpdateIntent = new Intent(
            ClockService.ACTION_UPDATE);

    private static final int REQUEST_CODE = 810528;

    private Context mContext = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        this.mContext = context;
        this.mContext.startService(mUpdateIntent);
        scheduleTimer();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] remainingIds = mgr.getAppWidgetIds(new ComponentName(context,
                this.getClass()));
        if (remainingIds == null || remainingIds.length <= 0) {
            PendingIntent pi = PendingIntent.getService(context, REQUEST_CODE,
                    mUpdateIntent, PendingIntent.FLAG_NO_CREATE);
            if (pi != null) {
                AlarmManager am = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                am.cancel(pi);
                pi.cancel();
                Log.d(TAG, "Alarm cancelled");
            }
        }
    }

    private void scheduleTimer() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.SECOND, 1);
        date.set(Calendar.MILLISECOND, 500);
        AlarmManager am = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(mContext, REQUEST_CODE,
                mUpdateIntent, PendingIntent.FLAG_NO_CREATE);
        if (pi == null) {
            pi = PendingIntent.getService(mContext, REQUEST_CODE,
                    mUpdateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setRepeating(AlarmManager.RTC, date.getTimeInMillis(), 1000, pi);
            Log.d(TAG, "Alarm created");
        }
    }
}
