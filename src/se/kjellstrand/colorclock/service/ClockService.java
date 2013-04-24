package se.kjellstrand.colorclock.service;

import java.util.Calendar;

import se.kjellstrand.colorclock.R;
import se.kjellstrand.colorclock.provider.ClockAppWidgetProvider;
import se.kjellstrand.colorclock.util.ColorUtil;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class ClockService extends IntentService {

    /**
     * Tag for logging
     */
    private static final String TAG = ClockService.class.getCanonicalName();

    public static final String ACTION_UPDATE = "se.kjellstrand.colorclock.ACTION_UPDATE";

    /**
     * List of the ids on the digit views.
     */
    private final static int[] DIGIT_VIEWS_INDEX = new int[] {
            R.id.digit_0, R.id.digit_1, R.id.digit_2, R.id.digit_3,
            R.id.digit_4, R.id.digit_5, R.id.digit_6, R.id.digit_7,
            R.id.digit_8, R.id.digit_9
    };

    /**
     * Holds the current colors of each digit, used while calculating the color
     * state of the clock in each update.
     */
    private final int[] mDigitsColor = new int[10];

    /**
     * Determines how strong the secondary color is, the color showing (shown in
     * caps) hH:mM:sS.
     */
    private double mSecondaryColorStrength = 0.7d;

    /**
     * Major color for hours, displayed on the first digit of the hours. So if
     * the clock is 12:34:56 then the 1 would get this color as background
     * color.
     */
    private int mPrimaryHourColor = 0xffff0000;

    /**
     * Minor color for hours, displayed on the second digit of the hours. So if
     * the clock is 12:34:56 then the 2 would get this color as background
     * color.
     */
    private int mSecondaryHourColor = ColorUtil
            .getSecondaryColorFromPrimaryColor(mPrimaryHourColor,
                    mSecondaryColorStrength);

    /**
     * Major color for minutes, displayed on the first digit of the minutes. So
     * if the clock is 12:34:56 then the 3 would get this color as background
     * color.
     */
    private int mPrimaryMinuteColor = 0xff00ff00;

    /**
     * Minor color for minutes, displayed on the second digit of the minutes. So
     * if the clock is 12:34:56 then the 4 would get this color as background
     * color.
     */
    private int mSecondaryMinuteColor = ColorUtil
            .getSecondaryColorFromPrimaryColor(mPrimaryMinuteColor,
                    mSecondaryColorStrength);

    /**
     * Major color for seconds, displayed on the first digit of the seconds. So
     * if the clock is 12:34:56 then the 5 would get this color as background
     * color.
     */
    private int mPrimarySecondColor = 0xff0000ff;

    /**
     * Minor color for seconds, displayed on the second digit of the seconds. So
     * if the clock is 12:34:56 then the 6 would get this color as background
     * color.
     */
    private int mSecondarySecondColor = ColorUtil
            .getSecondaryColorFromPrimaryColor(mPrimarySecondColor,
                    mSecondaryColorStrength);

    /**
     * What color will digits without a specific background set get, starts
     * uninitialised.
     */
    private int mDefaultDigitBackgrundColor;

    private AppWidgetManager mManager;

    private ComponentName mComponentName;

    public ClockService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mDefaultDigitBackgrundColor == 0) {
            mDefaultDigitBackgrundColor = getResources().getColor(
                    R.color.default_digit_background_color);
        }

        if (intent.getAction().equals(ACTION_UPDATE)) {
            Calendar now = Calendar.getInstance();
            updateAllViews(now);
        }
    }

    private void updateAllViews(Calendar calendar) {
        Log.d(TAG, "Update: " + calendar.getTime());
        if (mManager == null) {
            mManager = AppWidgetManager.getInstance(this);
        }
        if (mComponentName == null) {
            mComponentName = new ComponentName(this,
                    ClockAppWidgetProvider.class);
        }
        int[] appIds = mManager.getAppWidgetIds(mComponentName);
        for (int id : appIds) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.color_clock);
            updateView(remoteViews, calendar);
            mManager.updateAppWidget(id, remoteViews);
        }
    }

    /**
     * Updates the colors of the clock to a state representing "now".
     * 
     * @param calendar
     * 
     * @param context used to grab hold of some xml resource values such as
     *        default background color.
     */
    public void updateView(RemoteViews remoteViews, Calendar calendar) {

        if (mDefaultDigitBackgrundColor == -1) {
            mDefaultDigitBackgrundColor = getResources().getColor(
                    R.color.default_digit_background_color);
        }

        int hoursX0 = calendar.get(Calendar.HOUR_OF_DAY) / 10;
        int hours0X = calendar.get(Calendar.HOUR_OF_DAY) % 10;
        int minutesX0 = calendar.get(Calendar.MINUTE) / 10;
        int minutes0X = calendar.get(Calendar.MINUTE) % 10;
        int secondsX0 = calendar.get(Calendar.SECOND) / 10;
        int seconds0X = calendar.get(Calendar.SECOND) % 10;

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

        // Set the colors to the views.
        for (int i = 0; i <= 9; i++) {
            //
            remoteViews.setInt(DIGIT_VIEWS_INDEX[i], "setBackgroundColor",
                    mDigitsColor[i]);
        }
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
            return ColorUtil.screenBlendTwoColors(c1, c2);
        } else {
            return c2;
        }
    }

}
