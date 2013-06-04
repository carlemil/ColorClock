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
    public static int getSecondaryColorFromPrimaryColor(int color, double secondaryColorStrength) {
        // Retain the alpha channel
        return ((color & ALPHA_MASK)
                + ((int) ((color & RED_MASK) * secondaryColorStrength) & RED_MASK)
                + ((int) ((color & GREEN_MASK) * secondaryColorStrength) & GREEN_MASK)
                + ((int) ((color & BLUE_MASK) * secondaryColorStrength) & BLUE_MASK));
    }

    /**
     * Blend of color c1 and c2 by adding the components and dividing the
     * results with 2.
     * 
     * @param c1 first color to blend.
     * @param c2 second color to blend.
     * @return the result of blending color c1 and c2.
     */
    public static int averageBlendTwoColors(int c1, int c2) {
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

    /**
     * Blend of color c1 and c2 by applying f(c1, c2) = 1 - (1 - c1) * (1 - c2)
     * per channel.
     * 
     * @param c1 first color to blend.
     * @param c2 second color to blend.
     * @return the result of blending color c1 and c2.
     */
    public static int screenBlendTwoColors(int c1, int c2) {
        double a1 = Color.alpha(c1) / ((double) CHANNEL_MAX);
        double r1 = Color.red(c1) / ((double) CHANNEL_MAX);
        double g1 = Color.green(c1) / ((double) CHANNEL_MAX);
        double b1 = Color.blue(c1) / ((double) CHANNEL_MAX);

        double a2 = Color.alpha(c2) / ((double) CHANNEL_MAX);
        double r2 = Color.red(c2) / ((double) CHANNEL_MAX);
        double g2 = Color.green(c2) / ((double) CHANNEL_MAX);
        double b2 = Color.blue(c2) / ((double) CHANNEL_MAX);

        int a = (int) ((1 - (1 - a1) * (1 - a2)) * CHANNEL_MAX);
        int r = (int) ((1 - (1 - r1) * (1 - r2)) * CHANNEL_MAX);
        int g = (int) ((1 - (1 - g1) * (1 - g2)) * CHANNEL_MAX);
        int b = (int) ((1 - (1 - b1) * (1 - b2)) * CHANNEL_MAX);
        int c = Color.argb(a, r, g, b);

        return c;
    }

    /**
     * Blend of color c1 and c2 by applying f(c1, c2) = c1 * c2 per channel.
     * 
     * @param c1 first color to blend.
     * @param c2 second color to blend.
     * @return the result of blending color c1 and c2.
     */
    public static int multiplyBlendTwoColors(int c1, int c2) {
        double a1 = Color.alpha(c1) / ((double) CHANNEL_MAX);
        double r1 = Color.red(c1) / ((double) CHANNEL_MAX);
        double g1 = Color.green(c1) / ((double) CHANNEL_MAX);
        double b1 = Color.blue(c1) / ((double) CHANNEL_MAX);

        double a2 = Color.alpha(c2) / ((double) CHANNEL_MAX);
        double r2 = Color.red(c2) / ((double) CHANNEL_MAX);
        double g2 = Color.green(c2) / ((double) CHANNEL_MAX);
        double b2 = Color.blue(c2) / ((double) CHANNEL_MAX);

        int a = (int) ((a1 * a2) * CHANNEL_MAX);
        int r = (int) ((r1 * r2) * CHANNEL_MAX);
        int g = (int) ((g1 * g2) * CHANNEL_MAX);
        int b = (int) ((b1 * b2) * CHANNEL_MAX);
        int c = Color.argb(a, r, g, b);

        return c;
    }
}
