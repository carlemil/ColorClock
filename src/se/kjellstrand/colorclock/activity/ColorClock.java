package se.kjellstrand.colorclock.activity;

import se.kjellstrand.colorclock.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

public class ColorClock extends AppWidgetProvider {


    private static final String TAG = ColorClock.class.getName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        updateView(context);

    }

    public void updateView(Context context) {
        RemoteViews thisViews = new RemoteViews(context.getApplicationContext()
                .getPackageName(), R.layout.color_clock);
        Long t= System.currentTimeMillis()/10;
        thisViews.setTextViewText(R.id.digit_0, String.valueOf(t));
Log.d(TAG, "time: "+t);
        ComponentName thisWidget = new ComponentName(context,
                ColorClock.class);
        AppWidgetManager.getInstance(context).updateAppWidget(thisWidget,
                thisViews);
    }

}
