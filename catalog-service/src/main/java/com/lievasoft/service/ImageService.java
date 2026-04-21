package com.lievasoft.service;

import com.lievasoft.dto.response.DownloadImageResponse;
import com.lievasoft.dto.response.image.ImageSelectionResponse;

import java.util.List;

public interface ImageService {

    List<ImageSelectionResponse> obtainImagesToSelection(Long plantId);

    DownloadImageResponse obtainImagePlantBy(Long plantId, String filename);

    boolean setImageAsSelected(long plantId, long imageId);
}
