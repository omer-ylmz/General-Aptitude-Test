package com.gyt.searchservice.core.services;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.searchservice.business.messages.Messages;
import com.gyt.searchservice.core.services.models.DynamicFilter;
import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.core.services.models.DynamicSort;
import com.gyt.searchservice.core.services.models.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchManager implements SearchService {
    private final MongoTemplate mongoTemplate;
    private final MessageService messageService;

    @Override
    public <T> List<T> dynamicSearch(DynamicQuery dynamicQuery, Class<T> type) {
        Query query = new Query();
        if (dynamicQuery.filters() != null) {
            filter(query, dynamicQuery.filters());
        }
        if (dynamicQuery.sorts() != null) {
            sort(query, dynamicQuery.sorts());
        }
        paginate(query, dynamicQuery.pagination());
        return mongoTemplate.find(query, type);
    }

    private void filter(Query query, List<DynamicFilter> filters) {
        for (DynamicFilter filter : filters) {
            if (filter.field() == null || filter.field().isBlank())
                throw new IllegalArgumentException(this.messageService.getMessage(Messages.FilteringMessages.FIELD_MUST_BE_PROVIDED_FOR_FILTERING));
            if (filter.value() == null || filter.value().isBlank())
                throw new IllegalArgumentException(this.messageService.getMessage(Messages.FilteringMessages.VALUE_MUST_BE_PROVIDED_FOR_FILTERING));
            if (filter.operator() == null)
                throw new IllegalArgumentException(this.messageService.getMessage(Messages.FilteringMessages.OPERATOR_MUST_BE_PROVIDED_FOR_FILTERING));
        }

        filters.stream()
                .map(filter ->
                        switch (filter.operator()) {
                            case EQUALS -> Criteria.where(filter.field()).is(filter.value());
                            case NOT_EQUALS -> Criteria.where(filter.field()).ne(filter.value());
                            case GREATER_THAN -> Criteria.where(filter.field()).gt(filter.value());
                            case GREATER_THAN_OR_EQUALS -> Criteria.where(filter.field()).gte(filter.value());
                            case LESS_THAN -> Criteria.where(filter.field()).lt(filter.value());
                            case LESS_THAN_OR_EQUALS -> Criteria.where(filter.field()).lte(filter.value());
                            case IN -> Criteria.where(filter.field()).in((Object[]) filter.value().split(","));
                            case NOT_IN -> Criteria.where(filter.field()).nin((Object[]) filter.value().split(","));
                            case IS_NULL -> Criteria.where(filter.field()).is(null);
                            case IS_NOT_NULL -> Criteria.where(filter.field()).ne(null);
                            case STARTS_WITH -> Criteria.where(filter.field()).regex("^" + filter.value());
                            case ENDS_WITH -> Criteria.where(filter.field()).regex(filter.value() + "$");
                            case CONTAINS -> Criteria.where(filter.field()).regex(".*" + filter.value() + ".*");
                            case DOES_NOT_CONTAINS ->
                                    Criteria.where(filter.field()).not().regex(".*" + filter.value() + ".*");
                        }
                )
                .forEach(query::addCriteria);
    }

    private void sort(Query query, List<DynamicSort> sorts) {
        for (DynamicSort dynamicSort : sorts) {
            if (dynamicSort.field() == null || dynamicSort.field().isBlank())
                throw new IllegalArgumentException(this.messageService.getMessage(Messages.SortingMessages.FIELD_MUST_BE_PROVIDED_FOR_SORTING));
            if (dynamicSort.direction() == null)
                throw new IllegalArgumentException(this.messageService.getMessage(Messages.SortingMessages.DIRECTION_MUST_BE_PROVIDED_FOR_SORTING));
        }

        sorts.stream()
                .map(dynamicSort ->
                        switch (dynamicSort.direction()) {
                            case ASC -> Sort.by(Sort.Order.asc(dynamicSort.field()));
                            case DESC -> Sort.by(Sort.Order.desc(dynamicSort.field()));
                        }
                )
                .forEach(query::with);
    }

    private void paginate(Query query, Pagination pagination) {
        if (pagination == null)
            throw new IllegalArgumentException(this.messageService.getMessage(Messages.PaginationMessages.PAGINATION_MUST_BE_PROVIDED));
        if (pagination.page() < 0)
            throw new IllegalArgumentException(this.messageService.getMessage(Messages.PaginationMessages.PAGE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0));
        if (pagination.pageSize() < 1)
            throw new IllegalArgumentException(this.messageService.getMessage(Messages.PaginationMessages.PAGE_SIZE_MUST_BE_GREATER_THAN_0));
        Pageable pageable = PageRequest.of(pagination.page(), pagination.pageSize());
        query.with(pageable);
    }
}
