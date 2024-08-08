package com.gyt.searchservice.core.services;


import com.gyt.searchservice.core.services.models.DynamicQuery;

import java.util.List;

public interface SearchService {
    <T> List<T> dynamicSearch(DynamicQuery dynamicQuery, Class<T> type);
}
