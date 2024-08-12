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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/createExam")
    public ResponseEntity<CreateExamResponse> createExam(@RequestBody @Valid CreateExamRequest createExamRequest) {
        log.info("Received request to create exam: {}", createExamRequest);
        CreateExamResponse exam = examService.createExam(createExamRequest);
        log.info("Exam created successfully with ID: {}", exam.getId());
        return new ResponseEntity<>(exam, HttpStatus.CREATED);
    }

    @PutMapping("/updateExam")
    public ResponseEntity<UpdateExamResponse> updateExam(@RequestBody @Valid UpdateExamRequest updateExamRequest){
        log.info("Received request to update exam: {}", updateExamRequest);
        UpdateExamResponse updateExamResponse = examService.updateExam(updateExamRequest);
        log.info("Exam updated successfully with ID: {}", updateExamResponse.getId());
        return new ResponseEntity<>(updateExamResponse, HttpStatus.OK);
    }

    @GetMapping("/getExam/{id}")
    public ResponseEntity<GetExamResponse> getExamById(@PathVariable Long id){
        log.info("Received request to get exam by ID: {}", id);
        GetExamResponse exam = examService.getExamById(id);
        log.info("Retrieved exam with ID: {}", id);
        return new ResponseEntity<>(exam, HttpStatus.OK);
    }

    @GetMapping("/getAllExams")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllExamResponse> getAllQuestions(@RequestParam int page, @RequestParam int size) {
        log.info("Received request to get all exams with page: {} and size: {}", page, size);
        Page<GetAllExamResponse> allQuestion = examService.getAllExam(page, size);
        log.info("Retrieved {} exams", allQuestion.getTotalElements());
        return allQuestion;
    }

    @DeleteMapping("/deleteExam/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExamById(@PathVariable Long id){
        log.info("Received request to delete exam by ID: {}", id);
        examService.deleteExamById(id);
        log.info("Deleted exam with ID: {}", id);
    }

    @PostMapping("/addQuestionToExam")
    @ResponseStatus(HttpStatus.OK)
    public void addQuestionToExam(@RequestParam Long examId , Long questionId){
        log.info("Received request to add question with ID: {} to exam with ID: {}", questionId, examId);
        examService.addQuestionToExam(examId, questionId);
        log.info("Added question with ID: {} to exam with ID: {}", questionId, examId);
    }

    @PostMapping("/removeQuestionFromExam")
    @ResponseStatus(HttpStatus.OK)
    public void removeQuestionFromExam(@RequestParam Long examId , Long questionId){
        log.info("Received request to remove question with ID: {} from exam with ID: {}", questionId, examId);
        examService.removeQuestionFromExam(examId, questionId);
        log.info("Removed question with ID: {} from exam with ID: {}", questionId, examId);
    }

    @PostMapping("/extendExamEndDate")
    @ResponseStatus(HttpStatus.OK)
    public void extendExamEndDate(@RequestParam Long examId , LocalDateTime newEndDate){
        log.info("Received request to extend end date of exam with ID: {} to {}", examId, newEndDate);
        examService.extendExamEndDate(examId, newEndDate);
        log.info("Extended end date of exam with ID: {} to {}", examId, newEndDate);
    }
}
