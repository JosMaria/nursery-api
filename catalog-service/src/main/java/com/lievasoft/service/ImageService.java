package com.lievasoft.service;

import com.lievasoft.dto.response.image.ImageSelectionResponse;

import java.util.List;

public interface ImageService {

    List<ImageSelectionResponse> obtainImagesToSelection(Long plantId);
}
