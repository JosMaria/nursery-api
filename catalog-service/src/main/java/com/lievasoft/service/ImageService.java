package com.lievasoft.service;

import com.lievasoft.dto.response.image.PlantImageResponse;
import com.lievasoft.dto.response.image.DownloadImageResponse;
import com.lievasoft.dto.response.image.ImageSelectionResponse;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

public interface ImageService {

    PlantImageResponse persist(long plantId, boolean isSelected, FileUpload imageUpload);

    List<ImageSelectionResponse> obtainImagesSummaryToSelect(Long plantId);

    DownloadImageResponse obtainImagePlant(long plantId, long imageId);

    DownloadImageResponse obtainSelectedImagePlant(long plantId);

    boolean setImageAsSelected(long plantId, long imageId);
}
