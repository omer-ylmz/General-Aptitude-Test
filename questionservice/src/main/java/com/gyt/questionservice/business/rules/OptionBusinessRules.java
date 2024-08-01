package com.gyt.questionservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OptionBusinessRules {
    private final OptionRepository optionRepository;
    private final MessageService messageService;
    private final ManagementServiceClient managementServiceClient;



    public void upToFiveAnswerChecks(List<OptionDTO> options) {
        if (options.size() >= 5) {
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.MoreThanFiveAnswerErrors));
        }
    }

    public void correctOptionCheck(List<CreateOptionRequest> options) {

        for (CreateOptionRequest option : options) {
            if (option.getIsCorrect()) return;
        }
        throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.CorrectAnswerNotFoundError));
    }



    public void optionShouldBeExist(Long id) {
        Optional<Option> foundOption = optionRepository.findById(id);
        if (foundOption.isEmpty()) {
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist));
        }
    }

    public void textAndImageValidationRule(String text, String image) {
        if ((text.isEmpty() || text.isBlank()) && (image.isEmpty() || image.isBlank())) {
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.TextOrImageUrlError));
        }
    }

    public void validateCorrectOption(UpdateOptionRequest request) {
        Option existingOption = optionRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("Option not found"));

        if (Boolean.FALSE.equals(request.getIsCorrect())) {
            List<Option> allOptions = optionRepository.findAllByQuestionId(existingOption.getQuestion().getId());

            boolean hasAnotherCorrectOption = allOptions.stream()
                    .filter(option -> !option.getId().equals(existingOption.getId()))
                    .anyMatch(Option::getIsCorrect);

            if (!hasAnotherCorrectOption) {
                throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.AtLeastOneCorrectOptionRule));
            }
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

}
