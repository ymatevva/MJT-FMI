package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    private static final double RED_COEFFICIENT = 0.21;
    private static final double GREEN_COEFFICIENT = 0.72;
    private static final double BLUE_COEFFICIENT = 0.07;

    public LuminosityGrayscale() {
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("The image for luminosity grayscale cannot be null.");
        }

        BufferedImage blackWhiteImage =
            new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.getWidth(); i++) {

            for (int j = 0; j < image.getHeight(); j++) {

                int pixel = image.getRGB(i, j);
                Color color = new Color(pixel);

                int redValue = color.getRed();
                int blueValue = color.getBlue();
                int greenValue = color.getGreen();

                int newRGB =
                    (int) (redValue * RED_COEFFICIENT + blueValue * BLUE_COEFFICIENT + greenValue * GREEN_COEFFICIENT);

                Color newColor = new Color(newRGB, newRGB, newRGB);
                blackWhiteImage.setRGB(i, j, newColor.getRGB());
            }
        }

        return blackWhiteImage;
    }
}
