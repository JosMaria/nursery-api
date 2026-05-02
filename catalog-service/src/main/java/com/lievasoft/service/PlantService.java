package com.lievasoft.service;

import com.lievasoft.dto.request.plant.PlantCreateDTO;
import com.lievasoft.dto.response.plant.*;

import java.util.List;

public interface PlantService {

    PlantCreateResponse create(PlantCreateDTO plantCreateDTO);

    List<PlantSummaryResponse> obtainPlantSummaries();

    PaginatedResult<PlantCardResponse> obtainPlantCardPage(int numberPage, int sizePage);

    PlantDetailsResponse obtainPlantDetailsById(Long plantId);

    boolean changeFavoritePlant(long plantId, boolean isFavorite);
}
