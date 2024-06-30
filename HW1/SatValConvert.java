import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class SatValConvert implements PlugInFilter {

    public int setup(String args, ImagePlus im) {
        return DOES_RGB + DOES_STACKS;
    }

    public void run(ImageProcessor ip) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        float[] hsv = new float[3];

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                try {
                    int[] rgb = ip.getPixel(col, row, null);

                    Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv); // Converting RGB to HSV

                    float sbValue = hsv[1] * hsv[2]; // The product of saturation and brightness
                    int grayValue = (int) (sbValue * 255); // The grayscale value of the product

                    ip.putPixel(col, row, new int[]{grayValue, grayValue, grayValue});
                } catch (Exception e) {
                    IJ.log("Error processing pixel at (" + col + ", " + row + "): " + e.getMessage());
                }
            }
        }
    }
}

