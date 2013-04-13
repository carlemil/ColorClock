package se.kjellstrand.colorclock.util;


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
     * Number of bits to shift a byte to fully overlap the alpha channel
     */
    public static final int ALPHA_SHIFT = 24;

    /**
     * Number of bits to shift a byte to fully overlap the red channel
     */
    public static final int RED_SHIFT = 16;

    /**
     * Number of bits to shift a byte to fully overlap the green channel
     */
    public static final int GREEN_SHIFT = 8;

    /**
     * Number of bits to shift a byte to fully overlap the blue channel
     */
    public static final int BLUE_SHIFT = 0;

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
                + ((int) ((color & GREEN_MASK) * secondaryColorStrength) & GREEN_MASK) + ((int) ((color & BLUE_MASK) * secondaryColorStrength) & BLUE_MASK));
    }

    /**
     * Additive blends of color c1 and c2. The alpha channel is not blended
     * additively, but rather a mean is calculated by adding the two values and
     * shifting once to the right.
     * 
     * @param c1 first color to blend.
     * @param c2 second color to blend.
     * @return the result of blending color c1 and c2.
     */
    public static int additiveBlendTwoColors(int c1, int c2) {

        int a1 = (c1 & ALPHA_MASK) >> ALPHA_SHIFT;
        int r1 = (c1 & RED_MASK) >> RED_SHIFT;
        int g1 = (c1 & GREEN_MASK) >> GREEN_SHIFT;
        int b1 = (c1 & BLUE_MASK) >> BLUE_SHIFT;

        int a2 = (c2 & ALPHA_MASK) >> ALPHA_SHIFT;
        int r2 = (c2 & RED_MASK) >> RED_SHIFT;
        int g2 = (c2 & GREEN_MASK) >> GREEN_SHIFT;
        int b2 = (c2 & BLUE_MASK) >> BLUE_SHIFT;

        int c = (int) ((((a1 + a2) >> 1) & BYTE_MASK) << ALPHA_SHIFT)
                + (Math.min((r1 + r2) >> 1, CHANNEL_MAX) << RED_SHIFT)
                + (Math.min((g1 + g2) >> 1, CHANNEL_MAX) << GREEN_SHIFT)
                + (Math.min((b1 + b2) >> 1, CHANNEL_MAX) << BLUE_SHIFT);

        return c;
    }
}
