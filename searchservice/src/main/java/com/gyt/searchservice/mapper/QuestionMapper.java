package com.gyt.searchservice.mapper;


import com.gyt.corepackage.events.question.CreatedQuestionEvent;
import com.gyt.corepackage.events.question.UpdatedQuestionEvent;
import com.gyt.searchservice.models.entities.Question;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    Question createdQuestionEventToQuestion(CreatedQuestionEvent event);

    Question updatedQuestionEventToQuestion(UpdatedQuestionEvent event);
}