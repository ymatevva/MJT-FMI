package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements  FileSystemImageManager {

    public LocalFileSystemImageManager() {
    }

    /**
     * Loads a single image from the given file path.
     *
     * @param imageFile the file containing the image.
     * @return the loaded BufferedImage.
     * @throws IllegalArgumentException if the file is null
     * @throws IOException              if the file does not exist, is not a regular file,
     *                                  or is not in one of the supported formats.
     */
    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        validateFilePath(imageFile);
        validateImageFile(imageFile);
        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        validateDirectory(imagesDirectory);

        List<BufferedImage> images = new ArrayList<>();

        File[] imageFiles = imagesDirectory.listFiles();

        for (int i = 0; i < imageFiles.length ; i++) {
            validateImageFile(imageFiles[i]);
            BufferedImage currImage = ImageIO.read(imageFiles[i]);
            images.add(currImage);
        }
        return images;
    }

    /**
     * Saves the given image to the specified file path.
     *
     * @param image     the image to save.
     * @param imageFile the file to save the image to.
     * @throws IllegalArgumentException if the image or file is null.
     * @throws IOException              if the file already exists or the parent directory does not exist.
     */
    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("The image or the image file is null.");
        }

        if (imageFile.exists()) {
            throw new IOException("The file already exists.");
        }

        if ((imageFile.getParentFile() != null) && !imageFile.getParentFile().exists()) {
            throw new IOException("The parent directory does not exists.");
        }

        String format = getFileExtension(imageFile.getName()).toLowerCase();
        if (!List.of("jpeg", "png", "bmp").contains(format)) {
            throw new IOException("The image format is not supported.");
        }

        ImageIO.write(image, format, imageFile);
    }

    private String getFileExtension(String fileName) {
        int dotPos = fileName.lastIndexOf('.');
        if (dotPos != -1 && dotPos != fileName.length() - 1) {
            return fileName.substring(dotPos + 1);
        }
        return "";
    }

    private void validateImageFile(File imageFile) throws IOException {
        if (!imageFile.exists()) {
            throw new IOException("The file does not exist.");
        }

        if (!Files.isRegularFile(imageFile.toPath())) {
            throw new IOException("The file is not a regular file.");
        }

        String imageFormat = getFileExtension(imageFile.getName()).toLowerCase();
        if (!List.of("jpeg", "png", "bmp").contains(imageFormat)) {
            throw new IOException("The image format is not supported.");
        }
    }

    private void validateFilePath(File imagePath) {
        if (imagePath == null) {
            throw new IllegalArgumentException("The image path cannot be null.");
        }
    }

    private void validateDirectory(File directory) throws IOException {
        if (directory == null) {
            throw new IllegalArgumentException("The directory is null.");
        }

        if (!directory.exists()) {
            throw new IOException("The directory does not exists.");
        }

        if (!Files.isDirectory(directory.toPath())) {
            throw new IOException("The directory file is not a directory.");
        }
    }

}
