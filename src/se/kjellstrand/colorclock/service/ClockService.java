package se.kjellstrand.colorclock.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import se.kjellstrand.colorclock.R;
import se.kjellstrand.colorclock.activity.SettingsActivity;
import se.kjellstrand.colorclock.provider.ClockAppWidgetProvider;
import se.kjellstrand.colorclock.util.ColorUtil;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * A service that updates the clock widget whenever the updateAllViews method is
 * called.
 * 
 */
public class ClockService extends IntentService {

    /**
     * Tag for logging
     */
    private static final String TAG = ClockService.class.getCanonicalName();

    /**
     * String used to identify updates sent from the alarm.
     */
    public static final String ACTION_UPDATE = "se.kjellstrand.colorclock.ACTION_UPDATE";

    /**
     * If sSettingsChanged is true, we should re-read the settings on next
     * update of the clock. If sSettingsChanged is false, do nothing special.
     */
    private static boolean sSettingsChanged = false;

    /**
     * List of the ids on the digit views.
     */
    private final static int[] DIGIT_VIEWS_INDEX = new int[] {
            R.id.digit_0, R.id.digit_1, R.id.digit_2, R.id.digit_3, R.id.digit_4, R.id.digit_5, R.id.digit_6,
            R.id.digit_7, R.id.digit_8, R.id.digit_9
    };

    /**
     * A map from name of charsets to the R.array id containing the actual
     * digits of the charset.
     */
    private final static Map<String, Integer> charsetReversLookupMap = new HashMap<String, Integer>();

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
    private static int mPrimaryHourColor = 0;

    /**
     * Minor color for hours, displayed on the second digit of the hours. So if
     * the clock is 12:34:56 then the 2 would get this color as background
     * color.
     */
    private static int mSecondaryHourColor = 0;

    /**
     * Major color for minutes, displayed on the first digit of the minutes. So
     * if the clock is 12:34:56 then the 3 would get this color as background
     * color.
     */
    private static int mPrimaryMinuteColor = 0;

    /**
     * Minor color for minutes, displayed on the second digit of the minutes. So
     * if the clock is 12:34:56 then the 4 would get this color as background
     * color.
     */
    private static int mSecondaryMinuteColor = 0;

    /**
     * Major color for seconds, displayed on the first digit of the seconds. So
     * if the clock is 12:34:56 then the 5 would get this color as background
     * color.
     */
    private static int mPrimarySecondColor = 0;

    /**
     * Minor color for seconds, displayed on the second digit of the seconds. So
     * if the clock is 12:34:56 then the 6 would get this color as background
     * color.
     */
    private static int mSecondarySecondColor = 0;

    /**
     * What color will digits have.
     */
    private static int mDefaultDigitColor = 0;

    /**
     * What color will digits without a specific background set get, starts
     * uninitialised.
     */
    private static int mDefaultBackgrundColor = 0;

    /**
     * Manager of this widget.
     */
    private AppWidgetManager mManager;

    /**
     * Name of the class + package, used to get the list of id's for the
     * instances of this widget.
     */
    private ComponentName mComponentName;

    /**
     * Code used to identify a request.
     */
    private static final int REQUEST_CODE = 760315;

    /**
     * Constructor
     */
    public ClockService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        charsetReversLookupMap.clear();
        charsetReversLookupMap.put(getResources().getString(R.string.latin_charset), R.array.latin_digits);
        charsetReversLookupMap.put(getResources().getString(R.string.arabic_charset), R.array.arabic_digits);
        charsetReversLookupMap.put(getResources().getString(R.string.chinese_charset), R.array.chinese_digits);
        charsetReversLookupMap.put(getResources().getString(R.string.hardmode_charset), R.array.hardmode_digits);
        // Not supported by the default Android font
        charsetReversLookupMap.put(getResources().getString(R.string.khmer_charset), R.array.khmer_digits);

        // Force a read of the settings on first run.
        settingsChanged();
    }

    /**
     * Call when the settings have changed to trigger a re-read of the shared
     * prefs / settings.
     */
    public static void settingsChanged() {
        sSettingsChanged = true;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // if (mDefaultDigitBackgrundColor == 0) {
        // mDefaultDigitBackgrundColor =
        // getResources().getColor(R.color.default_background_color);
        // }

        if (mManager == null) {
            mManager = AppWidgetManager.getInstance(this);
        }

        if (mComponentName == null) {
            mComponentName = new ComponentName(this, ClockAppWidgetProvider.class);
        }

        if (intent.getAction().equals(ACTION_UPDATE)) {
            Calendar now = Calendar.getInstance();
            updateAllViews(now);
        }
    }

    /**
     * Walk the list of clock widgets and update them, one by one.
     * 
     * @param calendar the time used for the update.
     */
    private void updateAllViews(Calendar calendar) {

        int[] appIds = mManager.getAppWidgetIds(mComponentName);
        for (int id : appIds) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.color_clock);
            updateView(remoteViews, calendar);

            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.root_layout, pendingIntent);

            if (sSettingsChanged) {
                updateCharSetFromSharedPrefs(remoteViews);
            }

            mManager.updateAppWidget(id, remoteViews);
        }
        if (sSettingsChanged) {
            updateColorsFromSharedPrefs();
        }
        sSettingsChanged = false;
    }

    /**
     * Sets the colors found in the shared prefs.
     */
    private void updateColorsFromSharedPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mPrimaryHourColor = sharedPreferences.getInt(getResources().getString(R.string.pref_hour_color_key), 0);
        mPrimaryMinuteColor = sharedPreferences.getInt(getResources().getString(R.string.pref_minute_color_key), 0);
        mPrimarySecondColor = sharedPreferences.getInt(getResources().getString(R.string.pref_second_color_key), 0);

        mSecondaryHourColor = ColorUtil.getSecondaryColorFromPrimaryColor(mPrimaryHourColor,
                mSecondaryColorStrength);
        mSecondaryMinuteColor = ColorUtil.getSecondaryColorFromPrimaryColor(mPrimaryMinuteColor,
                mSecondaryColorStrength);
        mSecondarySecondColor = ColorUtil.getSecondaryColorFromPrimaryColor(mPrimarySecondColor,
                mSecondaryColorStrength);

        mDefaultBackgrundColor = sharedPreferences.getInt(getResources().getString(R.string.pref_background_color_key),
                0);
        mDefaultDigitColor = sharedPreferences.getInt(getResources().getString(R.string.pref_digit_color_key), 0);
    }

    /**
     * Sets the charset found in the shared prefs to all remote views.
     * 
     * @param remoteViews
     */
    private void updateCharSetFromSharedPrefs(RemoteViews remoteViews) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String newCharSet = sharedPreferences.getString(getResources().getString(R.string.pref_charsets_key),
                getResources().getString(R.string.latin_charset));

        int newCharSetId = charsetReversLookupMap.get(newCharSet);
        String[] chars = getResources().getStringArray(newCharSetId);

        for (int i = 0; i < chars.length; i++) {
            remoteViews.setTextViewText(DIGIT_VIEWS_INDEX[i], chars[i]);
            remoteViews.setTextColor(DIGIT_VIEWS_INDEX[i], mDefaultDigitColor);
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

        // Find out what boxes are 'active'
        int hoursX0 = calendar.get(Calendar.HOUR_OF_DAY) / 10;
        int hours0X = calendar.get(Calendar.HOUR_OF_DAY) % 10;
        int minutesX0 = calendar.get(Calendar.MINUTE) / 10;
        int minutes0X = calendar.get(Calendar.MINUTE) % 10;
        int secondsX0 = calendar.get(Calendar.SECOND) / 10;
        int seconds0X = calendar.get(Calendar.SECOND) % 10;

        // Reset all boxes to zero/black.
        for (int i = 0; i <= 9; i++) {
            mDigitsColor[i] = 0;
        }

        // Update the color of the boxes with 'active' digits in them..
        mDigitsColor[hoursX0] = setOrBlendDigitColorWithColor(mDigitsColor[hoursX0], mPrimaryHourColor);
        mDigitsColor[hours0X] = setOrBlendDigitColorWithColor(mDigitsColor[hours0X], mSecondaryHourColor);
        mDigitsColor[minutesX0] = setOrBlendDigitColorWithColor(mDigitsColor[minutesX0], mPrimaryMinuteColor);
        mDigitsColor[minutes0X] = setOrBlendDigitColorWithColor(mDigitsColor[minutes0X], mSecondaryMinuteColor);
        mDigitsColor[secondsX0] = setOrBlendDigitColorWithColor(mDigitsColor[secondsX0], mPrimarySecondColor);
        mDigitsColor[seconds0X] = setOrBlendDigitColorWithColor(mDigitsColor[seconds0X], mSecondarySecondColor);

        // For boxes without a color, set the default background color.
        for (int i = 0; i <= 9; i++) {
            if (mDigitsColor[i] == 0) {
                mDigitsColor[i] = mDefaultBackgrundColor;
            }
        }

        // Set the colors to the views.
        for (int i = 0; i <= 9; i++) {
            remoteViews.setInt(DIGIT_VIEWS_INDEX[i], "setBackgroundColor", mDigitsColor[i]);
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
