import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TestSobelEdgeDetection {

    private static final int KERNEL_SIZE = 3;
    private ImageAlgorithm grayscaleAlg;
    private ImageAlgorithm sobelEdgeDetection;
    private static final int MAX_SATURATION_VALUE = 255;


    @BeforeEach
    void setup() {
        grayscaleAlg = Mockito.mock(LuminosityGrayscale.class);
        sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlg);
    }

    @Test
    public void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> sobelEdgeDetection.process(null),
            "Sobel edge-detection algorithm should throw for image null.");
    }

    @Test
    public void testProcessWithImage() {

        // here is the image after luminosity alg

        BufferedImage greyImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        greyImage.setRGB(0, 0, 0xFF191919); // 25
        greyImage.setRGB(0, 1, 0xFF191919); // 25
        greyImage.setRGB(0, 2, 0xFF191919); // 25

        greyImage.setRGB(1, 0, 0xFF010101); // 1
        greyImage.setRGB(1, 1, 0xFF000000); // 0
        greyImage.setRGB(1, 2, 0xFF010101); // 1

        greyImage.setRGB(2, 0, 0xFF010101); // 1
        greyImage.setRGB(2, 1, 0xFF0A0A0A); // 10
        greyImage.setRGB(2, 2, 0xFF646464); // 100


        BufferedImage origImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        when(grayscaleAlg.process(Mockito.any())).thenReturn(greyImage);

        BufferedImage transformed = sobelEdgeDetection.process(origImage);

        Color defaultC = new Color(0, 0, 0);
        assertEquals(defaultC.getRGB(), transformed.getRGB(0, 0));
        assertEquals(defaultC.getRGB(), transformed.getRGB(0, 1));
        assertEquals(defaultC.getRGB(), transformed.getRGB(0, 2));

        assertEquals(transformed.getRGB(1, 0), defaultC.getRGB());
        Color edgeC = new Color(101, 101, 101);
        assertEquals(edgeC.getRGB(), transformed.getRGB(1, 1));

        assertEquals(transformed.getRGB(1, 2), defaultC.getRGB());

        assertEquals(transformed.getRGB(2, 0), defaultC.getRGB());
        assertEquals(transformed.getRGB(2, 1), defaultC.getRGB());
        assertEquals(transformed.getRGB(2, 2), defaultC.getRGB());

    }

}
