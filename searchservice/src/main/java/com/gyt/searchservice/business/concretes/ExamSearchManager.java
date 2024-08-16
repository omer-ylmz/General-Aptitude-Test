package com.gyt.searchservice.business.concretes;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.searchservice.business.abstracts.ExamSearchService;
import com.gyt.searchservice.core.services.SearchService;
import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.models.entities.Exam;
import com.gyt.searchservice.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamSearchManager implements ExamSearchService {
    private final ExamRepository examRepository;
    private final SearchService searchService;
    private final MessageService messageService;

    @Override
    public void add(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    public void update(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    public void delete(Long id) {
        examRepository.deleteById(id);
    }

    @Override
    public Exam getById(Long id) {
        return examRepository.findById(id).orElseThrow(
                () -> new BusinessException(messageService.getMessage("Boyle bir sinav bulunamadi."))
        );
    }

    @Override
    public List<Exam> searchQuestion(DynamicQuery dynamicQuery) {
        return searchService.dynamicSearch(dynamicQuery, Exam.class);
    }
}