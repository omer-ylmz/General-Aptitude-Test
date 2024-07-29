package com.gyt.questionservice.api.controllers;

import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<CreateQuestionResponse> createQuestion(@RequestBody CreateQuestionRequest createQuestionRequest) {
         return new ResponseEntity<>(questionService.createQuestion(createQuestionRequest), HttpStatus.CREATED);
    }

}
