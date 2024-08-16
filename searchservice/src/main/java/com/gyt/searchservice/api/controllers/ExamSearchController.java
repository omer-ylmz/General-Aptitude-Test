package com.gyt.searchservice.api.controllers;

import com.gyt.searchservice.business.abstracts.ExamSearchService;
import com.gyt.searchservice.core.services.models.DynamicQuery;
import com.gyt.searchservice.models.entities.Exam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("search-service/api/v1/exams")
public class ExamSearchController {
    private final ExamSearchService examSearchService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Exam> searchQuestion(@RequestBody DynamicQuery dynamicQuery){
        return examSearchService.searchQuestion(dynamicQuery);
    }


}
