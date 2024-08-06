package com.gyt.examservice.api.controllers;


import com.gyt.examservice.business.abstracts.ExamService;
import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/createExam")
    public ResponseEntity<CreateExamResponse> createExam(@RequestBody @Valid CreateExamRequest createExamRequest) {
        CreateExamResponse exam = examService.createExam(createExamRequest);
        return ResponseEntity.ok().body(exam);
    }
}
