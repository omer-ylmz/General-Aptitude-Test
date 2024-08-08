package com.gyt.examservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.examservice.api.clients.ManagementServiceClient;
import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.business.messages.Messages;
import com.gyt.examservice.dataAccess.abstracts.ExamRepository;
import com.gyt.examservice.entities.enums.Status;
import com.gyt.questionservice.GrpcGetQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ExamBusinessRules {
    private final MessageService messageService;
    private final ExamRepository examRepository;
    private final ManagementServiceClient managementServiceClient;

    public void validateExamDates(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();

        if (startDate.isBefore(now)) {
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.StartDateInPast));
        }
        if (endDate.isBefore(startDate)) {
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.EndDateBeforeStartDate));
        }
    }

    public void validateUniqueQuestions(List<Long> questionIds) {
        Set<Long> uniqueQuestions = new HashSet<>(questionIds);

        if (uniqueQuestions.size() != questionIds.size()) {
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
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.UserAuthorityError));
        }
    }

    public void checkIfExamCanBeModified(Status status) {
        if (status.equals(Status.IN_PROGRESS)) {
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamCannotBeModifiedBecauseItHasStarted));
        }
    }

    public void checkIfQuestionExistsInExam(List<Long> questionIds,Long id) {
        if (!questionIds.contains(id)) {
            throw new BusinessException(messageService.getMessage(Messages.ExamErrors.QuestionNotFoundInExam));
        }
    }
}
