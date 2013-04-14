package se.kjellstrand.colorclock.provider;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import se.kjellstrand.colorclock.R;
import se.kjellstrand.colorclock.util.ColorUtil;

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

    /**
     * 
     */
    private double mSecondaryColorStrength = 0.7d;

    /**
     * Major color for hours, displayed on the first digit of the hours. So if
     * the clock is 12:34:56 then the 1 would get this color as background
     * color.
     */
    private int mPrimaryHourColor = 0xd0f03020;

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
    private int mPrimaryMinuteColor = 0xd030f020;

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
    private int mPrimarySecondColor = 0xd03020f0;

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
        mSecondaryHourColor = ColorUtil.getSecondaryColorFromPrimaryColor(
                mPrimaryHourColor, mSecondaryColorStrength);
        mSecondaryMinuteColor = ColorUtil.getSecondaryColorFromPrimaryColor(
                mPrimaryMinuteColor, mSecondaryColorStrength);
        mSecondarySecondColor = ColorUtil.getSecondaryColorFromPrimaryColor(
                mPrimarySecondColor, mSecondaryColorStrength);
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

        int hoursX0 = c.get(Calendar.HOUR_OF_DAY) / 10;
        int hours0X = c.get(Calendar.HOUR_OF_DAY) % 10;
        int minutesX0 = c.get(Calendar.MINUTE) / 10;
        int minutes0X = c.get(Calendar.MINUTE) % 10;
        int secondsX0 = c.get(Calendar.SECOND) / 10;
        int seconds0X = c.get(Calendar.SECOND) % 10;

        for (int i = 0; i <= 9; i++) {
            mDigitsColor[i] = 0;
        }

        mDigitsColor[hoursX0] = setOrBlendDigitColorWithColor(
                mDigitsColor[hoursX0], mPrimaryHourColor);
        mDigitsColor[hours0X] = setOrBlendDigitColorWithColor(
                mDigitsColor[hours0X], mSecondaryHourColor);
        mDigitsColor[minutesX0] = setOrBlendDigitColorWithColor(
                mDigitsColor[minutesX0], mPrimaryMinuteColor);
        mDigitsColor[minutes0X] = setOrBlendDigitColorWithColor(
                mDigitsColor[minutes0X], mSecondaryMinuteColor);
        mDigitsColor[secondsX0] = setOrBlendDigitColorWithColor(
                mDigitsColor[secondsX0], mPrimarySecondColor);
        mDigitsColor[seconds0X] = setOrBlendDigitColorWithColor(
                mDigitsColor[seconds0X], mSecondarySecondColor);

        for (int i = 0; i <= 9; i++) {
            if (mDigitsColor[i] == 0) {
                mDigitsColor[i] = mDefaultDigitBackgrundColor;
            }
        }

//        Log.d(TAG,
//                "mDigitsColor[sec] "
//                        + Integer.toHexString(mDigitsColor[seconds0X]));

        // Set the colors to the views.
        for (int i = 0; i <= 9; i++) {
            //
            mRemoteViews.setInt(DIGIT_VIEWS_INDEX[i], "setBackgroundColor",
                    mDigitsColor[i]);
        }

        mWidgetManager.updateAppWidget(mAppWidgetIds, mRemoteViews);
    }

    /**
     * Blends the two colors unless the first color is pitch black with 0 alpha,
     * for that corner-case it will return the second color
     * 
     * @param c1 first color.
     * @param c2 second color.
     * @return a blend of the two color, unless above stated condition applies.
     */
    private int setOrBlendDigitColorWithColor(int c1, int c2) {
        if (c1 != 0) {
            return ColorUtil.blendTwoColors(c1, c2);
        } else {
            return c2;
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, "Shuting down Clock widget.",
                duration);
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
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Instaling Clock widget.",
                    duration);
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
