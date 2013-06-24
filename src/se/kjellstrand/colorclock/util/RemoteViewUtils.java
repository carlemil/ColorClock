package se.kjellstrand.colorclock.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.widget.RemoteViews;

import se.kjellstrand.colorclock.R;

public class RemoteViewUtils {

    /**
     * Tag for logging
     */
    private static final String TAG = RemoteViewUtils.class.getCanonicalName();

    private RemoteViewUtils() {
    }

    /**
     * Determine appropriate view based on width provided.
     *
     * @param context Context used for creating the new RemoteView.
     * @param height  Height of the widget.
     * @param width   Width of the widget.
     * @param layout  The layout id to use for the RemoteViews.
     * @return The RemoteViews, updated to display the new resided layout.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static RemoteViews getRemoteViews(final Context context, final int width,
                                             final int height, final int layout,
                                             final int textSize, final int[] DIGIT_VIEWS_INDEX) {

        float defaultSizeAdjuster = 50f;

        float textHeight;
        float textWidth;

        switch (layout) {
            case R.layout.color_clock_3x3:
                textHeight = (height / 3) * textSize / defaultSizeAdjuster;
                textWidth = (width / 3) * textSize / defaultSizeAdjuster;
                break;

            case R.layout.color_clock_2x5:
                textHeight = (height / 5) * textSize / defaultSizeAdjuster;
                textWidth = (width / 2) * textSize / defaultSizeAdjuster;
                break;

            case R.layout.color_clock_5x2:
                textHeight = (height / 2) * textSize / defaultSizeAdjuster;
                textWidth = (width / 5) * textSize / defaultSizeAdjuster;
                break;

            case R.layout.color_clock_3x4:
                textHeight = (height / 4) * textSize / defaultSizeAdjuster;
                textWidth = (width / 3) * textSize / defaultSizeAdjuster;
                break;

            case R.layout.color_clock_4x3:
                textHeight = (height / 3) * textSize / defaultSizeAdjuster;
                textWidth = (width / 4) * textSize / defaultSizeAdjuster;
                break;

            case R.layout.color_clock_1x10:
                textHeight = (height / 10) * textSize / defaultSizeAdjuster;
                textWidth = width * textSize / defaultSizeAdjuster;
                break;

            case R.layout.color_clock_10x1:
                textHeight = height * textSize / defaultSizeAdjuster;
                textWidth = (width / 10) * textSize / defaultSizeAdjuster;
                break;

            default:
                textHeight = 1;
                textWidth = 1;
                break;
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);

        for (int aDIGIT_VIEWS_INDEX : DIGIT_VIEWS_INDEX) {
            remoteViews.setTextViewTextSize(aDIGIT_VIEWS_INDEX, TypedValue.COMPLEX_UNIT_SP,
                    Math.min(textWidth, textHeight));
        }

        return remoteViews;
    }

}