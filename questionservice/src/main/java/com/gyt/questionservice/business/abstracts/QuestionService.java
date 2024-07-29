package com.gyt.questionservice.business.abstracts;

import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;

public interface QuestionService {
    CreateQuestionResponse createQuestion(CreateQuestionRequest request);
}
