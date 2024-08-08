package com.gyt.searchservice.business.messages;

public class Messages {

    public static class FilteringMessages {
        public static final String FIELD_MUST_BE_PROVIDED_FOR_FILTERING = "fieldMustBeProvidedForFiltering";
        public static final String VALUE_MUST_BE_PROVIDED_FOR_FILTERING = "valueMustBeProvidedForFiltering";
        public static final String OPERATOR_MUST_BE_PROVIDED_FOR_FILTERING = "operatorMustBeProvidedForFiltering";
    }

    public static class SortingMessages {
        public static final String FIELD_MUST_BE_PROVIDED_FOR_SORTING = "fieldMustBeProvidedForSorting";
        public static final String DIRECTION_MUST_BE_PROVIDED_FOR_SORTING = "directionMustBeProvidedForSorting";
    }

    public static class PaginationMessages {
        public static final String PAGINATION_MUST_BE_PROVIDED = "paginationMustBeProvided";
        public static final String PAGE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0 = "pageMustBeGreaterThanOrEqualTo0";
        public static final String PAGE_SIZE_MUST_BE_GREATER_THAN_0 = "pageSizeMustBeGreaterThan0";
    }
}
