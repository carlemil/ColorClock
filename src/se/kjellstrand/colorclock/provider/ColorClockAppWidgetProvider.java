package se.kjellstrand.colorclock.provider;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import se.kjellstrand.colorclock.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ColorClockAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = ColorClockAppWidgetProvider.class
            .getName();

    /**
     * List of the ids on the digit views.
     */
    private final static int[] DIGIT_VIEWS_INDEX = new int[] {
            R.id.digit_0, R.id.digit_1, R.id.digit_2, R.id.digit_3,
            R.id.digit_4, R.id.digit_5, R.id.digit_6, R.id.digit_7,
            R.id.digit_8, R.id.digit_9
    };

    /**
     * Updates AppWidget state; gets information about installed AppWidget
     * providers and other AppWidget related state.
     */
    private AppWidgetManager mWidgetManager;

    /**
     * The appWidgetIds for which an update is needed. Note that this may be all
     * of the AppWidget instances for this provider, or just a subset of them.
     */
    private int[] mAppWidgetIds;

    /**
     * Timer that fires once per second, and updates the colors of the clock.
     */
    private static Timer timer = null;

    /**
     * Holds the current colors of each digit, used while calculating the color
     * state of the clock in each update.
     */
    private final int[] mDigitsColor = new int[10];

    /**
     * Holds the views of the current widget layout.
     */
    private RemoteViews mRemoteViews;

    private double mSecondaryColorStrength = 0.7d;

    /**
     * Max value for the alpha channel in 32 bit argb. Used for bit
     * manipulations of the colors.
     */
    int mAlphaChannelMaxed = 0xff000000;

    /**
     * Major color for hours, displayed on the first digit of the hours. So if
     * the clock is 12:34:56 then the 1 would get this color as background
     * color.
     */
    private int mPrimaryHourColor = 0xffd03020;

    /**
     * Minor color for hours, displayed on the second digit of the hours. So if
     * the clock is 12:34:56 then the 2 would get this color as background
     * color.
     */
    private int mSecondaryHourColor = mPrimaryHourColor;

    /**
     * Major color for minutes, displayed on the first digit of the minutes. So
     * if the clock is 12:34:56 then the 3 would get this color as background
     * color.
     */
    private int mPrimaryMinuteColor = 0xff30d020;

    /**
     * Minor color for minutes, displayed on the second digit of the minutes. So
     * if the clock is 12:34:56 then the 4 would get this color as background
     * color.
     */
    private int mSecondaryMinuteColor = mPrimaryMinuteColor;

    /**
     * Major color for seconds, displayed on the first digit of the seconds. So
     * if the clock is 12:34:56 then the 5 would get this color as background
     * color.
     */
    private int mPrimarySecondColor = 0x8f3020d0;

    /**
     * Minor color for seconds, displayed on the second digit of the seconds. So
     * if the clock is 12:34:56 then the 6 would get this color as background
     * color.
     */
    private int mSecondarySecondColor = mPrimarySecondColor;

    /**
     * What color will digits without a specific background set get, starts
     * uninitialised.
     */
    private int mDefaultDigitBackgrundColor = -1;

    /**
     * Constructor, updates the secondary colors.
     */
    public ColorClockAppWidgetProvider() {
        upateSecondaryColors();
    }

    /**
     * Updates the secondary colors to be fractions of the primary colors.
     */
    private void upateSecondaryColors() {
        mSecondaryHourColor = getSecondaryColorFromPrimaryColor(mPrimaryHourColor);
        mSecondaryMinuteColor = getSecondaryColorFromPrimaryColor(mPrimaryMinuteColor);
        mSecondarySecondColor = getSecondaryColorFromPrimaryColor(mPrimarySecondColor);

        Log.d(TAG, "mSColor " + Integer.toHexString(mSecondarySecondColor));
        Log.d(TAG, "mMColor " + Integer.toHexString(mSecondaryMinuteColor));
        Log.d(TAG, "mHColor " + Integer.toHexString(mSecondaryHourColor));
    }

    /**
     * Take a color as input, multiply each of the rgb components by
     * mSecondaryColorStrength and return the new color that results from this.
     * 
     * @param color the primary color to pick rgb values from.
     * @return the secondary color.
     */
    private int getSecondaryColorFromPrimaryColor(int color) {
        // Retain the alpha channel
        return ((color & 0xff000000) 
                + ((int) ((color & 0xff0000) * mSecondaryColorStrength) & 0xff0000)
                + ((int) ((color & 0xff00) * mSecondaryColorStrength) & 0xff00) 
                + ((int) ((color & 0xff) * mSecondaryColorStrength) & 0xff0));
    }

    /**
     * Updates the colors of the clock to a state representing "now".
     * 
     * @param context used to grab hold of some xml resource values such as
     *        default background color.
     */
    public void update(Context context) {
        mRemoteViews = new RemoteViews(context.getPackageName(),
                R.layout.color_clock);

        if (mDefaultDigitBackgrundColor == -1) {
            mDefaultDigitBackgrundColor = context.getResources().getColor(
                    R.color.default_digit_background_color);
        }

        Calendar c = Calendar.getInstance();

        int s = c.get(Calendar.SECOND) % 10;
        int ss = c.get(Calendar.SECOND) / 10;
        int m = c.get(Calendar.MINUTE) % 10;
        int mm = c.get(Calendar.MINUTE) / 10;
        int h = c.get(Calendar.HOUR_OF_DAY) % 10;
        int hh = c.get(Calendar.HOUR_OF_DAY) / 10;

        for (int i = 0; i <= 9; i++) {
            mDigitsColor[i] = 0;
        }

        mDigitsColor[s] = additiveBlendTwoColors(mDigitsColor[s],
                mSecondarySecondColor);
        mDigitsColor[ss] = additiveBlendTwoColors(mDigitsColor[ss],
                mPrimarySecondColor);

        mDigitsColor[m] = additiveBlendTwoColors(mDigitsColor[m],
                mSecondaryMinuteColor);
        mDigitsColor[mm] = additiveBlendTwoColors(mDigitsColor[mm],
                mPrimaryMinuteColor);

        mDigitsColor[h] = additiveBlendTwoColors(mDigitsColor[h],
                mSecondaryHourColor);
        mDigitsColor[hh] = additiveBlendTwoColors(mDigitsColor[hh],
                mPrimaryHourColor);

        for (int i = 0; i <= 9; i++) {
            if (mDigitsColor[i] == 0) {
                mDigitsColor[i] = mDefaultDigitBackgrundColor;
            }
        }

        // Set the colors to the views.
        for (int i = 0; i <= 9; i++) {
            //
            mRemoteViews.setInt(DIGIT_VIEWS_INDEX[i], "setBackgroundColor",
                    mDigitsColor[i]);
        }

        mWidgetManager.updateAppWidget(mAppWidgetIds, mRemoteViews);
    }

    /**
     * Additive blends of color c1 and c2.
     * 
     * @param c1 first color to blend.
     * @param c2 second color to blend.
     * @return the result of blending color c1 and c2.
     */
    private int additiveBlendTwoColors(int c1, int c2) {
        int a = Math.min(((c1 & 0xff000000) + (c2 & 0xff000000)), 0xff000000);
        int r = Math.min(((c1 & 0xff0000) + (c2 & 0xff0000)), 0xff0000);
        int g = Math.min(((c1 & 0xff00) + (c2 & 0xff00)), 0xff00);
        int b = Math.min(((c1 & 0xff) + (c2 & 0xff)), 0xff);
        int c = a + r + g + b;
        return c;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        CharSequence text = "SHUTDOWN ";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        timer.cancel();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        // lets copy our stuff
        mAppWidgetIds = new int[appWidgetIds.length];
        for (int a = 0; a < appWidgetIds.length; a++)
            mAppWidgetIds[a] = appWidgetIds[a];
        mWidgetManager = appWidgetManager;

        update(context);

        if (timer == null) {
            CharSequence text = "INSTALLING";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            timer = new Timer();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 1);
            cal.set(Calendar.MILLISECOND, 500);

            timer.scheduleAtFixedRate(new MyTime(context, this), cal.getTime(),
                    1000);
        }

    }

    /**
     * Timer that sends updates once a second to the clock digits.
     * 
     */
    private class MyTime extends TimerTask {
        private ColorClockAppWidgetProvider parent;
        private Context context;

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
