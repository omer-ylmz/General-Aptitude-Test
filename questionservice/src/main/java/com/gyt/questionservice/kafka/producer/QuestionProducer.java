package com.gyt.questionservice.kafka.producer;

import com.gyt.corepackage.events.question.CreatedQuestionEvent;
import com.gyt.corepackage.events.question.DeletedQuestionEvent;
import com.gyt.corepackage.events.question.UpdatedQuestionEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendQuestionForCreate(CreatedQuestionEvent createdQuestionEvent) {
        LOGGER.info(String.format("Question created event =>%s", createdQuestionEvent));

        Message<CreatedQuestionEvent> message = MessageBuilder
                .withPayload(createdQuestionEvent)
                .setHeader(KafkaHeaders.TOPIC, "question-created")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendQuestionForUpdate(UpdatedQuestionEvent updatedQuestionEvent) {
        LOGGER.info(String.format("Question updated event =>%s", updatedQuestionEvent));

        Message<UpdatedQuestionEvent> message = MessageBuilder
                .withPayload(updatedQuestionEvent)
                .setHeader(KafkaHeaders.TOPIC, "question-updated")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendQuestionForDelete(DeletedQuestionEvent deletedQuestionEvent) {
        LOGGER.info(String.format("Question deleted event =>%s", deletedQuestionEvent));

        Message<DeletedQuestionEvent> message = MessageBuilder
                .withPayload(deletedQuestionEvent)
                .setHeader(KafkaHeaders.TOPIC, "question-deleted")
                .build();
        kafkaTemplate.send(message);
    }
}
