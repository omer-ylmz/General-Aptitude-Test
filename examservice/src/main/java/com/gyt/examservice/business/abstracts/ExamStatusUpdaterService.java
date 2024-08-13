package com.gyt.examservice.business.abstracts;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.examservice.api.clients.QuestionServiceClient;
import com.gyt.examservice.business.dtos.request.update.UpdateQuestionEditableRequest;
import com.gyt.examservice.business.messages.Messages;
import com.gyt.examservice.dataAccess.abstracts.ExamRepository;
import com.gyt.examservice.entities.concretes.Exam;
import com.gyt.examservice.entities.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamStatusUpdaterService {
    private final ExamRepository examRepository;
    private final QuestionServiceClient questionServiceClient;
    private final MessageService messageService;

    @Scheduled(fixedRate = 60000) // 1 dakikada bir çalışacak
    public void updateExamStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // Başlamış olan sınavları güncelle
        int startedExamsCount = examRepository.updateStatusToStarted(now);
        if (startedExamsCount > 0) {
            List<Exam> startedExams = examRepository.findAllByStatus(Status.IN_PROGRESS);
            startedExams.forEach(exam -> markQuestionsAsNotEditable(exam.getId()));
        }

        // Devam eden sınavları güncelle
        examRepository.updateStatusToFinished(now);

    }

    private void markQuestionsAsNotEditable(Long examId) {
        Exam exam = examRepository.findByIdWithQuestionIds(examId)
                .orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        UpdateQuestionEditableRequest request = new UpdateQuestionEditableRequest(exam.getQuestionIds(), false);


        if (exam.getStatus() == Status.IN_PROGRESS) {
            questionServiceClient.updateQuestionsEditableStatus(request);
        }
    }



}
