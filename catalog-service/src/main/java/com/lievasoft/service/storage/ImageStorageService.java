package com.lievasoft.service.storage;

import com.lievasoft.dto.plant.UploadImageResponse;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface ImageStorageService {

    UploadImageResponse uploadImageToFileSystem(Long plantId, FileUpload imageUpload);
}
