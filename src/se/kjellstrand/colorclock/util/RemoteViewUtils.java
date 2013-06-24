package se.kjellstrand.colorclock.util;

import android.content.Context;
import android.widget.RemoteViews;

import se.kjellstrand.colorclock.R;

public class RemoteViewUtils {

    private RemoteViewUtils() {
    }

    /**
     * Determine appropriate view based on width provided.
     *
     *
     * @param context Context used for creating the new RemoteView.
     * @param width   Width of the widget.
     * @param height  Height of the widget.
     * @param layout  The layout id to use for the RemoteViews.
     * @param textSize The relative size of the text/digits.
     * @return The RemoteViews, updated to display the new resided layout.
     */
    public static RemoteViews getRemoteViews(final Context context, final int width,
                                             final int height, final int layout,
                                             final float textSize,
                                             final int[] DIGIT_VIEWS_INDEX) {

        float textHeight;
        float textWidth;

        switch (layout) {
            case R.layout.color_clock_3x3:
                textHeight = (height / 3) * textSize;
                textWidth = (width / 3) * textSize;
                break;

            case R.layout.color_clock_2x5:
                textHeight = (height / 5) * textSize;
                textWidth = (width / 2) * textSize;
                break;

            case R.layout.color_clock_5x2:
                textHeight = (height / 2) * textSize;
                textWidth = (width / 5) * textSize;
                break;

            case R.layout.color_clock_3x4:
                textHeight = (height / 4) * textSize;
                textWidth = (width / 3) * textSize;
                break;

            case R.layout.color_clock_4x3:
                textHeight = (height / 3) * textSize;
                textWidth = (width / 4) * textSize;
                break;

            case R.layout.color_clock_1x10:
                textHeight = (height / 10) * textSize;
                textWidth = width * textSize;
                break;

            case R.layout.color_clock_10x1:
                textHeight = height * textSize;
                textWidth = (width / 10) * textSize;
                break;

            default:
                textHeight = 1;
                textWidth = 1;
                break;
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);

        for (int aDIGIT_VIEWS_INDEX : DIGIT_VIEWS_INDEX) {
            remoteViews.setFloat(aDIGIT_VIEWS_INDEX, "setTextSize",
                    Math.min(textWidth, textHeight));
        }

        return remoteViews;
    }

}