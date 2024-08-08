package com.gyt.searchservice.business.abstracts;

import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.models.entities.Question;

import java.util.List;

public interface QuestionSearchService {
    void add(Question question);

    void update(Question question);

    void delete(Long id);

    List<Question> searchQuestion(DynamicQuery dynamicQuery);

}
