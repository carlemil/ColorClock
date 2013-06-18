package se.kjellstrand.colorclock.util;

import android.content.Context;
import android.widget.RemoteViews;

import se.kjellstrand.colorclock.R;
import se.kjellstrand.colorclock.service.ClockService;

public class RemoteViewUtils {
    private RemoteViewUtils() {
    }

    /**
     * Determine appropriate view based on width provided.
     *
     * @param context Context used for creating the new RemoteView.
     * @param minWidth Minimum width of the widget.
     * @param minHeight Minimum height of the widget.
     * @return The RemoteViews, updated to display the new resided layout.
     */
    public static RemoteViews getRemoteViews(Context context, int minWidth, int minHeight) {
        // First find out rows and columns based on width provided.
        int rows = getCellsForSize(minHeight);
        int columns = getCellsForSize(minWidth);

        if (columns == rows) {
            if (columns == 1) {
                return new RemoteViews(context.getPackageName(),
                        R.layout.color_clock_1x1);
            } else {//if(columns==2){
                return new RemoteViews(context.getPackageName(),
                        R.layout.color_clock_2x2);
            }
        } else if (columns < rows) {
            return new RemoteViews(context.getPackageName(),
                    R.layout.color_clock_1x2);
        } else {
            return new RemoteViews(context.getPackageName(),
                    R.layout.color_clock_2x1);
        }
    }

    public static int getCellsForSize(int size) {
        // According to google specifications.
        if (size >= 250) {
            return 4;
        } else if (size >= 180) {
            return 3;
        } else if (size >= 110) {
            return 2;
        } else {
            return 1;
        }
    }
}