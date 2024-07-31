package com.gyt.questionservice.mapper;

import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import com.gyt.questionservice.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    Question createRequestToQuestion(CreateQuestionRequest createQuestionRequest);
    CreateQuestionResponse createQuestionToResponse(Question question);

    Question updateRequestToQuestion(UpdateQuestionRequest updateQuestionRequest);
    UpdateQuestionResponse updateQuestionToResponse(Question question);

    GetQuestionResponse getQuestionToResponse(Question question);

    GetAllQuestionResponse getAllQuestionToResponse(Question question);

    Question getResponseToQuestion(GetQuestionResponse getQuestionResponse);
}
