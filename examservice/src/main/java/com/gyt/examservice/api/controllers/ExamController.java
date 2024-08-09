package com.gyt.examservice.api.controllers;

import com.gyt.examservice.business.abstracts.ExamService;
import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.request.update.UpdateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetExamResponse;
import com.gyt.examservice.business.dtos.response.getAll.GetAllExamResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateExamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/createExam")
    public ResponseEntity<CreateExamResponse> createExam(@RequestBody @Valid CreateExamRequest createExamRequest) {
        CreateExamResponse exam = examService.createExam(createExamRequest);
        return new ResponseEntity<>(exam, HttpStatus.CREATED);
    }

    @PutMapping("/updateExam")
    public ResponseEntity<UpdateExamResponse> updateExam(@RequestBody @Valid UpdateExamRequest updateExamRequest){
        UpdateExamResponse updateExamResponse = examService.updateExam(updateExamRequest);
        return new ResponseEntity<>(updateExamResponse, HttpStatus.OK);
    }

    @GetMapping("/getExam/{id}")
    public ResponseEntity<GetExamResponse> getExamById(@PathVariable Long id){
        GetExamResponse exam = examService.getExamById(id);
        return new ResponseEntity<>(exam,HttpStatus.OK);
    }

    @GetMapping("/getAllExams")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllExamResponse> getAllQuestions(@RequestParam int page, @RequestParam int size) {
        Page<GetAllExamResponse> allQuestion = examService.getAllExam(page, size);
        return allQuestion;
    }

    @DeleteMapping("/deleteExam/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExamById(@PathVariable Long id){
        examService.deleteExamById(id);
    }

    @PostMapping("/addQuestionToExam")
    @ResponseStatus(HttpStatus.OK)
    public void addQuestionToExam(@RequestParam Long examId , Long questionId){
        examService.addQuestionToExam(examId,questionId);
    }

    @PostMapping("/removeQuestionFromExam")
    @ResponseStatus(HttpStatus.OK)
    public void removeQuestionFromExam(@RequestParam Long examId , Long questionId){
        examService.removeQuestionFromExam(examId,questionId);
    }

    @PostMapping("/extendExamEndDate")
    @ResponseStatus(HttpStatus.OK)
    public void extendExamEndDate(@RequestParam Long examId , LocalDateTime newEndDate){
        examService.extendExamEndDate(examId,newEndDate);
    }
}
