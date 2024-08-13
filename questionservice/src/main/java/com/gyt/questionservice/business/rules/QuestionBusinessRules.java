package com.gyt.questionservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionBusinessRules {
    private final MessageService messageService;
    private final ManagementServiceClient managementServiceClient;


    public void userAuthorizationCheck(Long creatorId) {
        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();
        boolean hasOrganizationRole = false;

        for (String role : authenticatedUser.getRoles()) {
            if (role.equals("organization")) {
                hasOrganizationRole = true;
            }
        }
        if (hasOrganizationRole && !authenticatedUser.getId().equals(creatorId)) {
            log.error("Question business rules - User with ID: {} is not authorized for question with creator ID: {}", authenticatedUser.getId(), creatorId);
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.UserAuthorityError));
        }
    }

    public void textAndImageValidationRule(String text, String image) {
        if ((text.isEmpty() || text.isBlank()) && (image.isEmpty() || image.isBlank())) {
            log.error("Both text and image URL are empty or blank");
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.TextOrImageUrlError));
        }
    }

    public void checkIfQuestionIsEditable(Boolean editable) {
        if (!editable) {
            log.error("Question update is restricted due to exam status started or finished");
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.QuestionUpdateRestrictedDueToExamStatus));
        }
    }
}
