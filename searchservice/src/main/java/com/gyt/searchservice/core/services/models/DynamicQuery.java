package com.gyt.searchservice.core.services.models;

import java.util.List;

public record DynamicQuery(
        List<DynamicFilter> filters,
        List<DynamicSort> sorts,
        Pagination pagination
) {
}
