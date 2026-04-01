package com.lievasoft.resource.validator;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class ImageValidator {

    public void validate(FileUpload imageUpload) {
        if (imageUpload == null || imageUpload.filePath() == null)
            throw new IllegalArgumentException("Image file is required");
    }
}
