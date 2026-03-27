package com.lievasoft.resource.validator;

import com.lievasoft.dto.plant.CommonNameCreateDTO;
import com.lievasoft.dto.plant.PlantCreateDTO;
import com.lievasoft.entity.Country;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class PlantValidator {

    public void validatePlantCreateDTO(PlantCreateDTO plantCreateDTO) {
        if (isScientificNameInvalid(plantCreateDTO.scientificName()))
            throw new IllegalArgumentException("scientific name must not be null, empty or blank");

        validateCommonNamesDTO(plantCreateDTO.commonNamesDTO());
    }

    private boolean isScientificNameInvalid(String scientificName) {
        return scientificName == null || scientificName.isBlank();
    }

    public void validateCommonNamesDTO(Collection<CommonNameCreateDTO> commonNamesToValidate) {
        if (isCollectionNullOrEmpty(commonNamesToValidate))
            throw new IllegalArgumentException("common names collection must not be null or empty");

        Collection<String> names = new ArrayList<>();
        List<Country> countries = new ArrayList<>();
        Collection<Boolean> selections = new ArrayList<>();
        for (var commonNameCreateDto : commonNamesToValidate) {
            names.add(commonNameCreateDto.name());
            countries.add(commonNameCreateDto.country());
            selections.add(commonNameCreateDto.isSelected());
        }

        if (hasSomeNameInvalid(names))
            throw new IllegalArgumentException("In common names exists values null, empty or blank");

        else if (hasDuplicateCountries(countries))
            throw new IllegalArgumentException("countries of the common names must be uniques");

        else if (existsMoreOneSelected(selections))
            throw new IllegalArgumentException("It must has zero or one values in selection");
    }

    private <T> boolean isCollectionNullOrEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    private boolean hasSomeNameInvalid(Collection<String> names) {
        return names.stream().anyMatch(name -> name == null || name.isBlank());
    }

    private boolean hasDuplicateCountries(List<Country> countries) {
        Set<Country> obtainedCountries = new HashSet<>();
        var isRepeat = false;
        var index = 0;

        do {
            var country = countries.get(index);
            isRepeat = !obtainedCountries.add(country);
            index++;

        } while (!isRepeat && index < countries.size());

        return isRepeat;
    }

    private boolean existsMoreOneSelected(Collection<Boolean> selections) {
        long selectedCount = selections.stream()
                .filter(selectedValue -> selectedValue)
                .count();

        return selectedCount > 1;
    }
}
