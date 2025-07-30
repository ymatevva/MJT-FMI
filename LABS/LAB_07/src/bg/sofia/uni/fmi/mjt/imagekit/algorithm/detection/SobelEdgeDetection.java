package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    private static final int KERNEL_SIZE = 3;
    private final ImageAlgorithm grayscaleAlg;
    private static final int MAX_SATURATION_VALUE = 255;

    private final int[][] horizontalKernel = new int[][] {
        {-1, 0, 1}
        , {-2, 0, 2}
        , {-1, 0, 1}
    };

    private final int[][] verticalKernel = new int[][] {
        {-1, -2, -1}
        , {0, 0, 0}
        , {1, 2, 1}
    };

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlg = grayscaleAlgorithm;
    }

    private void applyAlg(BufferedImage transformed, BufferedImage image, int startX,
                          int startY) {
        int gy = 0;
        int gx = 0;

        for (int i = 0; i < KERNEL_SIZE; i++) {
            for (int j = 0; j < KERNEL_SIZE; j++) {

                int pixel =  image.getRGB(i + startX, j + startY);
                Color color = new Color(pixel);
                int intensity = color.getRed();

                gx += intensity * horizontalKernel[i][j];
                gy += intensity * verticalKernel[i][j];
            }
        }

        int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
        magnitude = Math.min(MAX_SATURATION_VALUE, Math.max(0, magnitude));

        Color gray = new Color(magnitude, magnitude, magnitude);

        transformed.setRGB(startX + 1, startY + 1, gray.getRGB());
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("The image for sobel edge detection cannot be null.");
        }

        BufferedImage greyImage = grayscaleAlg.process(image);

        BufferedImage transformed =
            new BufferedImage(greyImage.getWidth(), greyImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < greyImage.getWidth() - KERNEL_SIZE; i++) {
            for (int j = 0; j < greyImage.getHeight() - KERNEL_SIZE; j++) {
                applyAlg(transformed, greyImage, i, j);
            }
        }
        return transformed;
    }

}
