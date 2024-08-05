package com.gyt.questionservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptionBusinessRules {
    private final OptionRepository optionRepository;
    private final MessageService messageService;
    private final ManagementServiceClient managementServiceClient;


    public void upToFiveAnswerChecks(int size) {
        if (size >= 5) {
            log.error("More than five options detected");
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.MoreThanFiveAnswerErrors));
        }
    }

    public void atLeastTwoAnswerChecks(List<Option> options) {
        if (options.size() == 2) {
            log.error("Only two options detected, at least two are required");
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.AtLeastTwoAnswerCheckErrors));
        }
    }

    public void correctOptionCheck(List<CreateOptionRequest> options) {

        for (CreateOptionRequest option : options) {
            if (option.getIsCorrect()) return;
        }
        log.error("No correct option found");
        throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.CorrectAnswerNotFoundError));
    }


    public void optionShouldBeExist(Long id) {
        Optional<Option> foundOption = optionRepository.findById(id);
        if (foundOption.isEmpty()) {
            log.error("Option with ID: {} does not exist", id);
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist));
        }
    }

    public void textAndImageValidationRule(String text, String image) {
        if ((text.isEmpty() || text.isBlank()) && (image.isEmpty() || image.isBlank())) {
            log.error("Both text and image are empty or blank");
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.TextOrImageUrlError));
        }
    }


    // TODO: 5.08.2024 son iki iş kuralı değerlendirelecek
    public void validateCorrectOption(UpdateOptionRequest request) {
        Option existingOption = optionRepository.findById(request.getId()).orElseThrow();

        if (Boolean.FALSE.equals(request.getIsCorrect())) {
            List<Option> allOptions = optionRepository.findAllByQuestionId(existingOption.getQuestion().getId());

            boolean hasAnotherCorrectOption = allOptions.stream()
                    .filter(option -> !option.getId().equals(existingOption.getId()))
                    .anyMatch(Option::getIsCorrect);

            if (!hasAnotherCorrectOption) {
                log.error("No other correct option found for question ID: {}", existingOption.getQuestion().getId());
                throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.AtLeastOneCorrectOptionRule));
            }
        }
    }

    public void ensureAtLeastOneCorrectOption(Long questionId, Long optionIdToDelete) {
        List<Option> options = optionRepository.findAllByQuestionId(questionId);

        boolean hasOtherCorrectOption = options.stream()
                .filter(option -> !option.getId().equals(optionIdToDelete))
                .anyMatch(Option::getIsCorrect);

        if (!hasOtherCorrectOption) {
            log.error("No other correct options found while deleting option for question id: {}", questionId);
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.AtLeastOneCorrectOptionRule));
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
            log.warn("Option business rules - User with ID: {} is not authorized for question with creator ID: {}", authenticatedUser.getId(), creatorId);
            throw new BusinessException(messageService.getMessage(Messages.QuestionErrors.UserAuthorityError));
        }
    }

}
