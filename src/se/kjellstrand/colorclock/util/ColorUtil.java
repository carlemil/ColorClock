package se.kjellstrand.colorclock.util;

import android.graphics.Color;

/**
 * Class providing operations on argb colors represented by the int primitive.
 * 
 * @author erbsman
 * 
 */
public class ColorUtil {

    /**
     * Max value for the alpha channel in 32 bit argb. Used for bit
     * manipulations of the colors.
     */
    public static final int ALPHA_MASK = 0xff000000;

    /**
     * Max value for the red channel in 32 bit argb. Used for bit manipulations
     * of the colors.
     */
    public static final int RED_MASK = 0xff0000;

    /**
     * Max value for the green channel in 32 bit argb. Used for bit
     * manipulations of the colors.
     */
    public static final int GREEN_MASK = 0xff00;

    /**
     * Max value for the blue channel in 32 bit argb. Used for bit manipulations
     * of the colors.
     */
    public static final int BLUE_MASK = 0xff;

    /**
     * Masks a full byte in bit operations.
     */
    public static final int BYTE_MASK = 0xff;

    /**
     * Max value for a byte, used for limiting a channels max value.
     */
    public static final int CHANNEL_MAX = 0xff;

    /**
     * Take a color as input, multiply each of the rgb components by
     * mSecondaryColorStrength and return the new color that results from this.
     * 
     * @param color the primary color to pick rgb values from.
     * @param secondaryColorStrength controls how much of the Primary color is
     *        left in the Secondary color.
     * @return the secondary color.
     */
    public static int getSecondaryColorFromPrimaryColor(int color,
            double secondaryColorStrength) {
        // Retain the alpha channel
        return ((color & ALPHA_MASK)
                + ((int) ((color & RED_MASK) * secondaryColorStrength) & RED_MASK)
                + ((int) ((color & GREEN_MASK) * secondaryColorStrength) & GREEN_MASK) 
                + ((int) ((color & BLUE_MASK) * secondaryColorStrength) & BLUE_MASK));
    }

    /**
     * Blends of color c1 and c2. 
     * 
     * @param c1 first color to blend.
     * @param c2 second color to blend.
     * @return the result of blending color c1 and c2.
     */
    public static int blendTwoColors(int c1, int c2) {
        int a1 = Color.alpha(c1);
        int r1 = Color.red(c1);
        int g1 = Color.green(c1);
        int b1 = Color.blue(c1);

        int a2 = Color.alpha(c2);
        int r2 = Color.red(c2);
        int g2 = Color.green(c2);
        int b2 = Color.blue(c2);

        int a = Math.min((a1 + a2) >> 1, CHANNEL_MAX);
        int r = Math.min((r1 + r2) >> 1, CHANNEL_MAX);
        int g = Math.min((g1 + g2) >> 1, CHANNEL_MAX);
        int b = Math.min((b1 + b2) >> 1, CHANNEL_MAX);
        int c = Color.argb(a, r, g, b);

        return c;
    }
}
