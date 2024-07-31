package com.gyt.questionservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OptionBusinessRules {
    private final OptionRepository optionRepository;
    private final MessageService messageService;
    private final ManagementServiceClient managementServiceClient;


    public void upToFiveAnswerChecks(List<OptionDTO> options){
        if(options.size() >= 5){
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.MoreThanFiveAnswerErrors));
        }
    }

    public void correctOptionCheck(List<OptionDTO> options, boolean requestIsCorrect) {
        boolean hasCorrectOption = false;

        // Mevcut seçeneklerin kontrolü
        for (OptionDTO option : options) {
            Optional<Option> foundOption = optionRepository.findById(option.getId());
            if (foundOption.isPresent() && foundOption.get().getIsCorrect()) {
                hasCorrectOption = true;
                break;
            }
        }

        // Yeni eklenmekte olan seçeneğin kontrolü
        if (requestIsCorrect) {
            hasCorrectOption = true;
        }

        // Eğer toplamda 5. seçenek eklenirken doğru cevap yoksa hata fırlat
        if (options.size() == 4 && !hasCorrectOption) {
            throw new BusinessException(messageService.getMessage(Messages.OptionsErrors.CorrectAnswerNotFoundError));
        }
    }
    public void optionShouldBeExist(Long id){
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
