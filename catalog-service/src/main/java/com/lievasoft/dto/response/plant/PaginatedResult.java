package com.lievasoft.dto.response.plant;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaginatedResult<T>(
        List<T> content,
        int page,
        int size,
        @JsonProperty("total_elements")
        long totalElements,
        @JsonProperty("total_pages")
        int totalPages
) {
}
