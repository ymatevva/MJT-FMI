package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestLocalFileSystemImageManager {

    private LocalFileSystemImageManager localFileSystemImageManager;

    // TO DO: test save method and make tests where everything should work
    
    @BeforeEach
    void setup() {
        localFileSystemImageManager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageWithNull() {
        assertThrows(IllegalArgumentException.class, () -> localFileSystemImageManager.loadImage(null),
            "Load image method should throw when image is null.");
    }

    @Test
    void testLoadImageWithNonExistentImage() {
        File fakeFile = Mockito.mock(File.class);
        assertThrows(IOException.class, () -> localFileSystemImageManager.loadImage(fakeFile),
            "Load image method should throw when image path is non existent.");
    }

    @Test
    void testLoadImageWithNotRegularFile() {

        File file = Mockito.mock(File.class);
        when(file.isFile()).thenReturn(false);
        assertThrows(IOException.class, () -> localFileSystemImageManager.loadImage(file),
            "Load image method should throw when image path is not a regular file.");
    }

    @Test
    void testLoadImageWithWrongFileFormat() {
        try {
            File temp = File.createTempFile("image", ".svg");
            temp.deleteOnExit();

            assertThrows(IOException.class, () -> localFileSystemImageManager.loadImage(temp),
                "Should throw if file extension is not supported");

        } catch (IOException e) {
            fail("Failed to create test file");
        }
    }

    @Test
    void testLoadImageWhenFormatIsAllowed() {
        try {
            File temp = Files.createTempFile("kitten", ".jpeg").toFile();
            temp.deleteOnExit();
            assertDoesNotThrow(() -> localFileSystemImageManager.loadImage(temp),
                "Load image does not throw when format is among the allowed ones");

        } catch (IOException e) {
            fail("Cant create the test");
        }
    }

    @Test
    void testLoadImagesFromDirectoryNull() {
        assertThrows(IllegalArgumentException.class, () -> localFileSystemImageManager.loadImagesFromDirectory(null),
            "Method should throw with null directory.");
    }

    @Test
    void testLoadImagesFromDirectoryNonExistent() {
        assertThrows(IOException.class, () -> localFileSystemImageManager.loadImagesFromDirectory(new File("smth")),
            "Method should throw with not existing directory.");
    }

    @Test
    void testLoadImagesFromDirectoryNotDirectory() {
        File path = Mockito.mock(File.class);
        when(path.isDirectory()).thenReturn(false);
        assertThrows(IOException.class, () -> localFileSystemImageManager.loadImagesFromDirectory(path),
            "Method should throw with not  directory.");
    }

    @Test
    void testLoadImagesFromDirWithInvalidFile() throws IOException {

        Path dir = Files.createTempDirectory("temp-dir");
        dir.toFile().deleteOnExit();

        File firstImage = new File(dir.toFile(), "kitten.png");
        firstImage.createNewFile();
        firstImage.deleteOnExit();

        File secondImage = new File(dir.toFile(), "kitten.gmp");
        secondImage.createNewFile();
        secondImage.deleteOnExit();

        assertThrows(IOException.class, () -> localFileSystemImageManager.loadImagesFromDirectory(dir.toFile()),
            "Method should throw with invalid file in directory.");
    }
}
