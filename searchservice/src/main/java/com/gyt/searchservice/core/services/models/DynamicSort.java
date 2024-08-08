package com.gyt.searchservice.core.services.models;


import com.gyt.searchservice.core.services.enums.SortDirection;

public record DynamicSort(
        String field,
        SortDirection direction
) {
}
