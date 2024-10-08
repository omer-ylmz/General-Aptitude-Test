package com.gyt.examservice.business.abstracts;

import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.request.update.UpdateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.examservice.business.dtos.response.getAll.GetAllExamResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateExamResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExamService {
    CreateExamResponse createExam(CreateExamRequest createExamRequest);
    UpdateExamResponse updateExam(UpdateExamRequest updateExamRequest);
    GetExamResponse getExamById(Long id);
    Page<GetAllExamResponse> getAllExam (int page, int size);
    void deleteExamById(Long id);
    void addQuestionToExam(Long examId, Long questionId);
    void removeQuestionFromExam(Long examId, Long questionId);
    void extendExamEndDate(Long examId, LocalDateTime newEndDate);
    List<GetQuestionResponse> fetchAndMapQuestions(List<Long> questionIds);
}
