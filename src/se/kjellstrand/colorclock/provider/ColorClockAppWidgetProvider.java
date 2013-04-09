package se.kjellstrand.colorclock.provider;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import se.kjellstrand.colorclock.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ColorClockAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = ColorClockAppWidgetProvider.class
            .getName();

    int[] mAppWidgetIds;
    AppWidgetManager widgetManager;
    static Timer timer = null;

    public ColorClockAppWidgetProvider() {
        Log.d(TAG, "ColorClockAppWidgetProvider");

    }

    public void update(Context context) {
        Log.d("tag","updating");
        
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.color_clock);

        views.setTextViewText(R.id.digit_0,
                String.valueOf((System.currentTimeMillis()/1000) % 10));

        widgetManager.updateAppWidget(mAppWidgetIds, views);

    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        CharSequence text = "SHUTDOWN ";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        timer.cancel();
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        // lets copy our stuff
        mAppWidgetIds = new int[appWidgetIds.length];
        for (int a = 0; a < appWidgetIds.length; a++)
            mAppWidgetIds[a] = appWidgetIds[a];
        widgetManager = appWidgetManager;

        update(context);

        if (timer == null) {
            CharSequence text = "INSTALLING";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            timer = new Timer();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 1);
            cal.set(Calendar.MILLISECOND, 0);

            timer.scheduleAtFixedRate(new MyTime(context, this), cal.getTime(),
                    1000);
        }

    }

    private class MyTime extends TimerTask {
        AppWidgetManager appWidgetManager;
        ColorClockAppWidgetProvider parent;
        Context context;

        public MyTime(Context context, ColorClockAppWidgetProvider parent) {
            this.parent = parent;
            this.context = context;
        }

        @Override
        public void run() {
            try {
                parent.update(context);
            } catch (Exception e) {
                CharSequence text = "tim_excp : " + e.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

}