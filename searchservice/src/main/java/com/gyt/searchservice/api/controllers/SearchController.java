package com.gyt.searchservice.api.controllers;

import com.gyt.searchservice.business.abstracts.QuestionSearchService;
import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.models.entities.Question;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("search-service/api/v1/questions")
public class SearchController {
    private final QuestionSearchService questionSearchService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Question> searchQuestion(@RequestBody DynamicQuery dynamicQuery){
        return questionSearchService.searchQuestion(dynamicQuery);
    }
}
