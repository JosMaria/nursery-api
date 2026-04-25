package com.lievasoft.service;

import com.lievasoft.dto.response.DownloadImageResponse;
import com.lievasoft.dto.response.image.ImageSelectionResponse;

import java.util.List;

public interface ImageService {

    List<ImageSelectionResponse> obtainImagesSummaryToSelect(Long plantId);

    DownloadImageResponse obtainImagePlant(long plantId, long imageId);

    DownloadImageResponse obtainSelectedImagePlant(long plantId);

    boolean setImageAsSelected(long plantId, long imageId);
}
