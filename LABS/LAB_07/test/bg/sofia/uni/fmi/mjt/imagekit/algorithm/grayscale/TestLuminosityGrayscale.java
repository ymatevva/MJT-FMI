package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

public class TestLuminosityGrayscale {

    private ImageAlgorithm luminosityGrayscale;

    private static final double RED_COEFFICIENT = 0.21;
    private static final double GREEN_COEFFICIENT = 0.72;
    private static final double BLUE_COEFFICIENT = 0.07;
    private static final int IMAGE_SIZE = 2;
    private static final int MAX_INTENSITY_VALUE = 255;

    @BeforeEach
    void setup() {
        luminosityGrayscale = new LuminosityGrayscale();
    }

    @Test
    public void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class,  () -> luminosityGrayscale.process(null), "Process method should throw when image is null.");
    }

    @Test
    public void testProcessWithImage() {

        BufferedImage origImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE,  BufferedImage.TYPE_INT_RGB);

        origImage.setRGB(0,0, Color.RED.getRGB());
        origImage.setRGB(0,1, Color.RED.getRGB());
        origImage.setRGB(1,0, Color.GREEN.getRGB());
        origImage.setRGB(1,1, Color.BLUE.getRGB());

        BufferedImage grey = luminosityGrayscale.process(origImage);

        int redPixelTransformed = (int) (RED_COEFFICIENT * MAX_INTENSITY_VALUE);
        Color onlyRed = new Color(redPixelTransformed,redPixelTransformed,redPixelTransformed);

        int greenPixelTransformed = (int) (GREEN_COEFFICIENT * MAX_INTENSITY_VALUE);
        Color onlyGreen = new Color(greenPixelTransformed,greenPixelTransformed,greenPixelTransformed);

        int bluePixelTransformed = (int) (BLUE_COEFFICIENT * MAX_INTENSITY_VALUE);
        Color onlyBlue = new Color(bluePixelTransformed,bluePixelTransformed,bluePixelTransformed);

        assertTrue(grey.getRGB(0, 0) == onlyRed.getRGB(), String.format("Expected red pixel value after grayscale algorithm is not a match. Instead value is: %d", grey.getRGB(0,0)));
        assertTrue(grey.getRGB(0, 1) == onlyRed.getRGB(), String.format("Expected red pixel value after grayscale algorithm is not a match. Instead value is: %d", grey.getRGB(0,1)));
        assertTrue(grey.getRGB(1, 0) == onlyGreen.getRGB(), String.format("Expected green pixel value after grayscale algorithm is not a match. Instead value is: %d", grey.getRGB(1,0)));
        assertTrue(grey.getRGB(1, 1) == onlyBlue.getRGB(), String.format("Expected blue pixel value after grayscale algorithm is not a match. Instead value is: %d", grey.getRGB(1,1)));
    }
}
