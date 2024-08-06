package com.gyt.questionservice.api.controllers;

import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<CreateQuestionResponse> createQuestion(@RequestBody @Valid CreateQuestionRequest createQuestionRequest) {
        log.info("Create request received for question: {} {}", createQuestionRequest.getText(), createQuestionRequest.getImageUrl());
        CreateQuestionResponse response = questionService.createQuestion(createQuestionRequest);
        log.info("Question created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateQuestionResponse> updateQuestion(@RequestBody @Valid UpdateQuestionRequest updateQuestionRequest) {
        log.info("Update request received for question with ID: {}", updateQuestionRequest.getId());
        UpdateQuestionResponse response = questionService.updateQuestion(updateQuestionRequest);
        log.info("Question with ID: {} updated successfully", response.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getByID/{id}")
    public ResponseEntity<GetQuestionResponse> getQuestionById(@PathVariable Long id) {
        log.info("Get request received for question with ID: {}", id);
        GetQuestionResponse response = questionService.getQuestionByID(id);
        log.info("Question with ID: {} retrieved successfully", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getall")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllQuestionResponse> getAllQuestions(@RequestParam int page, @RequestParam int size) {
        log.info("Get all questions request received for page: {}, size: {}", page, size);
        Page<GetAllQuestionResponse> allQuestion = questionService.getAllQuestion(page, size);
        log.info("Questions retrieved successfully for page: {}, size: {}", page, size);
        return allQuestion;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestionByID(@PathVariable Long id) {
        log.info("Delete request received for question with ID: {}", id);
        questionService.deleteQuestionByID(id);
        log.info("Question with ID: {} deleted successfully", id);

    }

    @PostMapping("/addOptionToQuestion/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    public CreateOptionResponse addOptionToQuestion(@PathVariable Long questionId, @RequestBody @Valid CreateOptionRequest createOptionRequest) {
        log.info("Add option request received for question with ID: {}", questionId);
        CreateOptionResponse response = questionService.addOptionToQuestion(questionId, createOptionRequest);
        log.info("Option added to question with ID: {} successfully", questionId);
        return response;
    }


}
