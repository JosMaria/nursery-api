package com.lievasoft.dto.request.plant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lievasoft.entity.Country;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonNameCreateDTO(
        String name,
        Country country,
        String place,

        @JsonProperty("is_selected")
        boolean isSelected
) {
}
