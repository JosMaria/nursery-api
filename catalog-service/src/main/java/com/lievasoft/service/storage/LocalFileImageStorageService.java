package com.lievasoft.service.storage;

import com.lievasoft.dto.mapping.UploadImageResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ApplicationScoped
public class LocalFileImageStorageService implements ImageStorageService {

    private static final Logger LOG = Logger.getLogger(LocalFileImageStorageService.class);

    @Override
    public UploadImageResponse uploadImageToFileSystem(Long plantId, FileUpload imageUpload) {
        final var imageDir = "/Users/josmaria/Pictures/plant_images";
        Path filePath = imageUpload.uploadedFile();
        if (filePath != null && Files.exists(filePath)) {
            try {
                Path directoryPath = Paths.get(imageDir, "plant_%s".formatted(plantId));
                if (!Files.exists(directoryPath))
                    Files.createDirectories(directoryPath);

                byte[] imageBytes = Files.readAllBytes(filePath);
                var imageExtension = getExtensionByContentType(imageUpload.contentType());
                var filename = UUID.randomUUID() + "." + imageExtension.getExtension();
                var filePathToUpload = directoryPath.resolve(filename);
                Files.write(filePathToUpload, imageBytes);
                LOG.infof("Image saved in filesystem at directory: %s, filename: %s", directoryPath, filename);
                return new UploadImageResponse(directoryPath.toString(), filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else throw new IllegalArgumentException("Image file path is invalid");
    }

    @Override
    public byte[] downloadImageFromFileSystem(String filename, String directoryPath) {
        var imagePath = Paths.get(directoryPath, filename);
        var file = new File(imagePath.toString());
        if (file.exists()) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        } else throw new IllegalArgumentException("Image file not found");
    }

    private ImageExtension getExtensionByContentType(String contentType) {
        return switch (contentType) {
            case "image/webp" -> ImageExtension.WEBP;
            case "image/gif" -> ImageExtension.GIF;
            case "image/png" -> ImageExtension.PNG;
            case "image/jpeg" -> ImageExtension.JPEG;
            case "image/jpg" -> ImageExtension.JPG;
            default -> throw new IllegalArgumentException("Invalid content type for image");
        };
    }

    private enum ImageExtension {
        WEBP("webp"),
        GIF("gif"),
        PNG("png"),
        JPEG("jpeg"),
        JPG("jpg");

        private final String extension;

        ImageExtension(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return this.extension;
        }
    }
}
