package com.gyt.examservice.producer;

import com.gyt.corepackage.events.exam.*;
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
public class ExamProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendExamForCreate(CreatedExamEvent createdExamEvent) {
        LOGGER.info(String.format("Exam created event =>%s", createdExamEvent));

        Message<CreatedExamEvent> message = MessageBuilder
                .withPayload(createdExamEvent)
                .setHeader(KafkaHeaders.TOPIC, "exam-created")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendExamForUpdate(UpdatedExamEvent updatedExamEvent) {
        LOGGER.info(String.format("Question updated event =>%s", updatedExamEvent));

        Message<UpdatedExamEvent> message = MessageBuilder
                .withPayload(updatedExamEvent)
                .setHeader(KafkaHeaders.TOPIC, "exam-updated")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendExamForDelete(DeletedExamEvent deletedExamEvent) {
        LOGGER.info(String.format("Question updated event =>%s", deletedExamEvent));

        Message<DeletedExamEvent> message = MessageBuilder
                .withPayload(deletedExamEvent)
                .setHeader(KafkaHeaders.TOPIC, "exam-deleted")
                .build();
        kafkaTemplate.send(message);
    }


    public void sendExamForAddedQuestion(AddedQuestionToExamEvent addedQuestionToExamEvent) {
        LOGGER.info(String.format("Question added for exam event =>%s", addedQuestionToExamEvent));

        Message<AddedQuestionToExamEvent> message = MessageBuilder
                .withPayload(addedQuestionToExamEvent)
                .setHeader(KafkaHeaders.TOPIC, "exam-for-added-question")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendExamForRemovedQuestion(RemovedQuestionToExamEvent removedQuestionToExamEvent) {
        LOGGER.info(String.format("Question removed for exam event =>%s", removedQuestionToExamEvent));

        Message<RemovedQuestionToExamEvent> message = MessageBuilder
                .withPayload(removedQuestionToExamEvent)
                .setHeader(KafkaHeaders.TOPIC, "exam-for-removed-question")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendExamEndDateExtended(EndDateExtendedForExamEvent endDateExtendedForExamEvent) {
        LOGGER.info(String.format("Exam end date extended event =>%s", endDateExtendedForExamEvent));

        Message<EndDateExtendedForExamEvent> message = MessageBuilder
                .withPayload(endDateExtendedForExamEvent)
                .setHeader(KafkaHeaders.TOPIC, "exam-endate-extended")
                .build();
        kafkaTemplate.send(message);
    }
}
