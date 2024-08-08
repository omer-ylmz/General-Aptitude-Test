package com.gyt.searchservice.business.concretes;

import com.gyt.searchservice.business.abstracts.QuestionSearchService;
import com.gyt.searchservice.core.services.SearchService;
import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.models.entities.Question;
import com.gyt.searchservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionSearchManager implements QuestionSearchService {
    private final QuestionRepository questionRepository;
    private final SearchService searchService;

    @Override
    public void add(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void update(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void delete(Long id) {
        questionRepository.deleteById(id);

    }

    @Override
    public List<Question> searchQuestion(DynamicQuery dynamicQuery) {
        return searchService.dynamicSearch(dynamicQuery, Question.class);
    }
}
