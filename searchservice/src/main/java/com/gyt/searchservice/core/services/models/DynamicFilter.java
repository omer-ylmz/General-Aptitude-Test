package com.gyt.searchservice.core.services.models;

import com.gyt.searchservice.core.services.enums.FilterOperator;

public record DynamicFilter(
        String field,
        FilterOperator operator,
        String value
) {
}
