package com.gyt.searchservice.business.abstracts;

import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.models.entities.Exam;

import java.util.List;

public interface ExamSearchService {

    void add(Exam exam);

    void update(Exam exam);

    void delete(Long id);

    Exam getById(Long id);

    List<Exam> searchQuestion(DynamicQuery dynamicQuery);
}


