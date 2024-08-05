package com.gyt.questionservice.business.abstracts;

import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import org.springframework.data.domain.Page;

public interface QuestionService {
    CreateQuestionResponse createQuestion(CreateQuestionRequest request);
    UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request);
    GetQuestionResponse getQuestionByID(Long id);
    Page<GetAllQuestionResponse> getAllQuestion (int page,int size);
    void deleteQuestionByID(Long id);
    CreateOptionResponse addOptionToQuestion(Long questionId, CreateOptionRequest request);



}
