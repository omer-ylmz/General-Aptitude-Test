package com.gyt.questionservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionBusinessRules {
    private final QuestionRepository questionRepository;
    private final MessageService messageService;
    private final ManagementServiceClient managementServiceClient;

    public void questionShouldBeExist(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.QuestionShouldBeExist));
        }
    }

    public void userAuthorizationCheck(Long creatorId) {
        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();
        boolean hasOrganizationRole = false;

        for (String role : authenticatedUser.getRoles()) {
            if (role.equals("organization")) {
                hasOrganizationRole = true;
            }
        }
        if (hasOrganizationRole && !authenticatedUser.getId().equals(creatorId)) {
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.UserAuthorityError));
        }
    }

    public void textAndImageValidationRule(String text, String image) {
        if ((text == null || text.isBlank()) && (image == null || image.isBlank())) {
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.TextOrImageUrlError));
        }
    }
}
