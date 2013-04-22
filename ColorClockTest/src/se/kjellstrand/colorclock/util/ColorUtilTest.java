/**
 * 
 */
package se.kjellstrand.colorclock.util;

import junit.framework.TestCase;

/**
 * @author erbsman
 * 
 */
public class ColorUtilTest extends TestCase {

    /**
     * @param name
     */
    public ColorUtilTest(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link se.kjellstrand.colorclock.util.ColorUtil#getSecondaryColorFromPrimaryColor(int, double)}
     * .
     */
    public void testGetSecondaryColorFromPrimaryColor() {
        // Check that the alpha channel is not affected.
        assertEquals(0x20000000,
                ColorUtil.getSecondaryColorFromPrimaryColor(0x20000000, 0.2d));
        assertEquals(0x7d000000,
                ColorUtil.getSecondaryColorFromPrimaryColor(0x7d000000, 0.5d));
        assertEquals(0xfd000000,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xfd000000, 0.7d));

        // Check rgb separately.
        assertEquals(0xff800000,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xffa00000, 0.8d));
        assertEquals(0xff008000,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xff00a000, 0.8d));
        assertEquals(0xff000080,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xff0000a0, 0.8d));

        // check agb together.
        assertEquals(0xff010101,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xff0a0a0a, 0.1d));
        assertEquals(0xff101010,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xffa0a0a0, 0.1d));
        assertEquals(0xff103341,
                ColorUtil.getSecondaryColorFromPrimaryColor(0xff206682, 0.5d));
    }

    
    /**
     * Test method for
     * {@link se.kjellstrand.colorclock.util.ColorUtil#screenBlendTwoColors(int, int)}
     * .
     */
    public void testScreenBlendTwoColors() {
        // Test the alpha channel
        assertEquals(0x00000000,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x00000000) & ColorUtil.ALPHA_MASK);
        assertEquals(0x3f000000,
                ColorUtil.screenBlendTwoColors(0x22000000, 0x22000000) & ColorUtil.ALPHA_MASK);
        assertEquals(0xb6000000,
                ColorUtil.screenBlendTwoColors(0x77000000, 0x77000000) & ColorUtil.ALPHA_MASK);
        assertEquals(0x21000000,
                ColorUtil.screenBlendTwoColors(0x00ffffff, 0x22654321) & ColorUtil.ALPHA_MASK);
        assertEquals(0x21000000,
                ColorUtil.screenBlendTwoColors(0x00123456, 0x22ffffff) & ColorUtil.ALPHA_MASK);
        assertEquals(0x21000000,
                ColorUtil.screenBlendTwoColors(0x00ffffff, 0x22ffffff) & ColorUtil.ALPHA_MASK);
        assertEquals(0xd7000000,
                ColorUtil.screenBlendTwoColors(0x88456789, 0xaa234789) & ColorUtil.ALPHA_MASK);
        assertEquals(0x08000000,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x08000000));
        assertEquals(0xf7000000,
                ColorUtil.screenBlendTwoColors(0xc0000000, 0xe0000000));
        assertEquals(0x08000000,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x08000000));

        // Test the red channel
        assertEquals(0x00ff0000,
                ColorUtil.screenBlendTwoColors(0x00ff0000, 0x00ff0000) & ColorUtil.RED_MASK);
        assertEquals(0x00ff0000,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x00ff0000) & ColorUtil.RED_MASK);
        assertEquals(0x008e0000,
                ColorUtil.screenBlendTwoColors(0x00440000, 0x00660000) & ColorUtil.RED_MASK);
        
        // Test the green channel
        assertEquals(0x0000ff00,
                ColorUtil.screenBlendTwoColors(0x0000ff00, 0x0000ff00) & ColorUtil.GREEN_MASK);
        assertEquals(0x0000ff00,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x0000ff00) & ColorUtil.GREEN_MASK);
        assertEquals(0x00008e00,
                ColorUtil.screenBlendTwoColors(0x00004400, 0x00006600) & ColorUtil.GREEN_MASK);

        // Test the blue channel
        assertEquals(0x000000ff,
                ColorUtil.screenBlendTwoColors(0x000000ff, 0x000000ff) & ColorUtil.BLUE_MASK);
        assertEquals(0x000000ff,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x000000ff) & ColorUtil.BLUE_MASK);
        assertEquals(0x0000008e,
                ColorUtil.screenBlendTwoColors(0x00000044, 0x00000066) & ColorUtil.BLUE_MASK);
        
        // Test the all channels
        assertEquals(0xc7c7c7c7,
                ColorUtil.screenBlendTwoColors(0x88888888, 0x88888888));
        assertEquals(0x88888888,
                ColorUtil.screenBlendTwoColors(0x00000000, 0x88888888));
        assertEquals(0x88888888,
                ColorUtil.screenBlendTwoColors(0x88888888, 0x00000000));
    }
    
    /**
     * Test method for
     * {@link se.kjellstrand.colorclock.util.ColorUtil#averageBlendTwoColors(int, int)}
     * .
     */
    public void testAverageBlendTwoColors() {
        // Test the alpha channel
        assertEquals(0x00000000,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x00000000) & ColorUtil.ALPHA_MASK);
        assertEquals(0x11000000,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x22000000) & ColorUtil.ALPHA_MASK);
        assertEquals(0x77000000,
                ColorUtil.averageBlendTwoColors(0x77000000, 0x77000000) & ColorUtil.ALPHA_MASK);
        assertEquals(0x11000000,
                ColorUtil.averageBlendTwoColors(0x00ffffff, 0x22654321) & ColorUtil.ALPHA_MASK);
        assertEquals(0x11000000,
                ColorUtil.averageBlendTwoColors(0x00123456, 0x22ffffff) & ColorUtil.ALPHA_MASK);
        assertEquals(0x11000000,
                ColorUtil.averageBlendTwoColors(0x00ffffff, 0x22ffffff) & ColorUtil.ALPHA_MASK);
        assertEquals(0x99000000,
                ColorUtil.averageBlendTwoColors(0x88456789, 0xaa234789) & ColorUtil.ALPHA_MASK);
        assertEquals(0x04000000,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x08000000));
        assertEquals(0xd0000000,
                ColorUtil.averageBlendTwoColors(0xc0000000, 0xe0000000));
        assertEquals(0x04000000,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x08000000));

        // Test the red channel
        assertEquals(0x00ff0000,
                ColorUtil.averageBlendTwoColors(0x00ff0000, 0x00ff0000) & ColorUtil.RED_MASK);
        assertEquals(0x007F0000,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x00ff0000) & ColorUtil.RED_MASK);
        assertEquals(0x00550000,
                ColorUtil.averageBlendTwoColors(0x00440000, 0x00660000) & ColorUtil.RED_MASK);
        
        // Test the green channel
        assertEquals(0x0000ff00,
                ColorUtil.averageBlendTwoColors(0x0000ff00, 0x0000ff00) & ColorUtil.GREEN_MASK);
        assertEquals(0x00007f00,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x0000ff00) & ColorUtil.GREEN_MASK);
        assertEquals(0x00005500,
                ColorUtil.averageBlendTwoColors(0x00004400, 0x00006600) & ColorUtil.GREEN_MASK);

        // Test the blue channel
        assertEquals(0x000000ff,
                ColorUtil.averageBlendTwoColors(0x000000ff, 0x000000ff) & ColorUtil.BLUE_MASK);
        assertEquals(0x0000007f,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x000000ff) & ColorUtil.BLUE_MASK);
        assertEquals(0x00000055,
                ColorUtil.averageBlendTwoColors(0x00000044, 0x00000066) & ColorUtil.BLUE_MASK);
        
        // Test the all channels
        assertEquals(0x88888888,
                ColorUtil.averageBlendTwoColors(0x88888888, 0x88888888));
        assertEquals(0x44444444,
                ColorUtil.averageBlendTwoColors(0x00000000, 0x88888888));
        assertEquals(0x44444444,
                ColorUtil.averageBlendTwoColors(0x88888888, 0x00000000));
    }

}
