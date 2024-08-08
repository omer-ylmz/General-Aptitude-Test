package com.gyt.searchservice.kafka.consumer;

import com.gyt.corepackage.events.question.CreatedQuestionEvent;
import com.gyt.corepackage.events.question.DeletedQuestionEvent;
import com.gyt.corepackage.events.question.UpdatedQuestionEvent;
import com.gyt.searchservice.business.abstracts.QuestionSearchService;
import com.gyt.searchservice.mapper.QuestionMapper;
import com.gyt.searchservice.models.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionConsumer {

    private final QuestionMapper questionMapper;
    private final QuestionSearchService questionSearchService;

    @KafkaListener(topics = "question-created", groupId = "search")
    public void consumeCreatedQuestionEvent(CreatedQuestionEvent event) {
        Question question = questionMapper.createdQuestionEventToQuestion(event);
        questionSearchService.add(question);
    }

    @KafkaListener(topics = "question-updated", groupId = "search")
    public void consumeUpdatedQuestionEvent(UpdatedQuestionEvent event) {
        Question question = questionMapper.updatedQuestionEventToQuestion(event);
        questionSearchService.update(question);
    }

    @KafkaListener(topics = "question-deleted", groupId = "search")
    public void consumeDeletedQuestionEvent(DeletedQuestionEvent event) {
        questionSearchService.delete(event.getId());
    }



}
