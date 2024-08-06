package com.gyt.examservice.business.abstracts;

import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;

public interface ExamService {
    CreateExamResponse createExam(CreateExamRequest createExamRequest);
}
