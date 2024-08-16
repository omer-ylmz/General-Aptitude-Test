package com.gyt.searchservice.kafka.consumer;

import com.gyt.corepackage.events.exam.*;
import com.gyt.searchservice.business.abstracts.ExamSearchService;
import com.gyt.searchservice.mapper.ExamMapper;
import com.gyt.searchservice.models.entities.Exam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// TODO: 16.08.2024 Loglanacak

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamConsumer {
    private final ExamMapper examMapper;
    private final ExamSearchService examSearchService;

    @KafkaListener(topics = "exam-created", groupId = "search")
    public void consumeCreatedExamEvent(CreatedExamEvent event) {
        log.info("Received CreatedExamEvent with ID: {}", event.getId());

        Exam exam = examMapper.createdExamEventToExam(event);
        examSearchService.add(exam);

        log.info("Question with ID: {} has been added.", exam.getId());
    }

    @KafkaListener(topics = "exam-updated", groupId = "search")
    public void consumeUpdatedExamEvent(UpdatedExamEvent event) {
        Exam exam = examMapper.updatedExamEventToExam(event);
        examSearchService.update(exam);
    }

    @KafkaListener(topics = "exam-deleted", groupId = "search")
    public void consumeDeletedExamEvent(DeletedExamEvent event) {
        examSearchService.delete(event.getId());
    }


    @KafkaListener(topics = "exam-for-added-question", groupId = "search")
    public void consumeQuestionAddedToExamEvent(AddedQuestionToExamEvent event){
        Exam exam = examSearchService.getById(event.getExamId());
        exam.setQuestionSize(exam.getQuestionSize() + 1); // questionSize propertysi olduğu varsayılıyor.
        examSearchService.add(exam);

        log.info("Added question size for exam ID: {}", event.getExamId());
    }

    @KafkaListener(topics = "exam-for-removed-question", groupId = "search")
    public void consumeQuestionRemovedToExamEvent(RemovedQuestionToExamEvent event){
        Exam exam = examSearchService.getById(event.getExamId());
        exam.setQuestionSize(exam.getQuestionSize() - 1);
        examSearchService.add(exam);

        log.info("Removed question size for exam ID: {}", event.getExamId());
    }

    // TODO: 16.08.2024 sınav tarihleri doğru göndermesine rağmen search farklı kaydediyor
    @KafkaListener(topics = "exam-for-removed-question", groupId = "search")
    public void consumeEndDateExtendedForExamEvent(EndDateExtendedForExamEvent event){
        Exam exam = examSearchService.getById(event.getExamId());
        exam.setEndDate(event.getNewEndDate());
        examSearchService.add(exam);

        log.info("End date extended for exam ID: {}", event.getExamId());
    }


}
