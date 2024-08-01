package com.gyt.questionservice.api.controllers;

import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<CreateQuestionResponse> createQuestion(@RequestBody @Valid CreateQuestionRequest createQuestionRequest) {
        return new ResponseEntity<>(questionService.createQuestion(createQuestionRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateQuestionResponse> updateQuestion(@RequestBody UpdateQuestionRequest updateQuestionRequest) {
        return new ResponseEntity<>(questionService.updateQuestion(updateQuestionRequest), HttpStatus.OK);
    }

    @GetMapping("/getByID/{id}")
    public ResponseEntity<GetQuestionResponse> getQuestionById(@PathVariable Long id) {
        return new ResponseEntity<>(questionService.getQuestionByID(id), HttpStatus.OK);
    }

    @GetMapping("/getall")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllQuestionResponse> getAllQuestions(@RequestParam int page, @RequestParam int size) {
        return questionService.getAllQuestion(page, size);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestionByID(@PathVariable Long id){
        questionService.deleteQuestionByID(id);
    }


}
