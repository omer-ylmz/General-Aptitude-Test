package com.gyt.examservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.business.messages.Messages;
import com.gyt.examservice.entities.concretes.Exam;
import com.gyt.examservice.entities.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExamBusinessRules {
    private final MessageService messageService;


    public void validateExamDates(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();

        if (startDate.isBefore(now)) {
            log.error("Start date is in the past: {}", startDate);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.StartDateInPast));
        }
        if (endDate.isBefore(startDate)) {
            log.error("End date is before start date: startDate={}, endDate={}", startDate, endDate);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.EndDateBeforeStartDate));
        }
    }

    public void validateUniqueQuestions(List<Long> questionIds) {
        Set<Long> uniqueQuestions = new HashSet<>(questionIds);

        if (uniqueQuestions.size() != questionIds.size()) {
            log.error("Duplicate questions found: {}", questionIds);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.DuplicateQuestionsNotAllowed));
        }
    }

    public void userAuthorizationCheck(Long creatorId,GetUserResponse authenticatedUser) {

        boolean hasOrganizationRole = false;

        for (String role : authenticatedUser.getRoles()) {
            if (role.equals("organization")) {
                hasOrganizationRole = true;
            }
        }
        if (hasOrganizationRole && !authenticatedUser.getId().equals(creatorId)) {
            log.error("User {} is not authorized to perform this action", authenticatedUser.getId());
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.UserAuthorityError));
        }
    }

    public void checkIfExamCanBeModified(Status status) {
        if (status.equals(Status.IN_PROGRESS)) {
            log.error("Exam cannot be modified because it has started: status={}", status);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamCannotBeModifiedBecauseItHasStarted));
        }
    }

    public void checkIfQuestionExistsInExam(List<Long> questionIds,Long id) {
        if (!questionIds.contains(id)) {
            log.error("Question not found in exam: questionId={}", id);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.QuestionNotFoundInExam));
        }
    }

    public void checkIfQuestionAlreadyExistsInExam(Exam exam, Long questionId){
        if(exam.getQuestionIds().contains(questionId)){
            log.error("Question already exists in exam: questionId={}", questionId);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.QuestionAlreadyExistsInExam));
        }
    }

    public void checkIfLastQuestionInExam(Exam exam){
        if (exam.getQuestionIds().size() == 1) {
            log.error("Cannot remove the last question from the exam: examId={}", exam.getId());
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.LastQuestionCanNotBeRemoved));
        }
    }

    public void checkIfExamIsInProgress(Status status) {
        if (status != Status.IN_PROGRESS) {
            log.error("Invalid exam status for extension: status={}", status);
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.InvalidExamStatusForExtension));
        }
    }
}
