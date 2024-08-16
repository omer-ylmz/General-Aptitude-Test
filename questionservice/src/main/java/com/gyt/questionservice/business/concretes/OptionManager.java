package com.gyt.questionservice.business.concretes;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.business.rules.QuestionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.mapper.OptionMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptionManager implements OptionService {
    private final OptionRepository optionRepository;
    private final OptionBusinessRules optionBusinessRules;
    private final QuestionBusinessRules questionBusinessRules;
    private final MessageService messageService;
    private final OptionMapper optionMapper;


    @Override
    public UpdateOptionResponse updateOption(UpdateOptionRequest request) {
        log.info("Update request received for option with ID: {}", request.getId());

        Option existingOption = optionRepository.findById(request.getId()).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist))
        );

        optionBusinessRules.userAuthorizationCheck(existingOption.getQuestion().getCreatorId());
        questionBusinessRules.checkIfQuestionIsEditable(existingOption.getQuestion().getIsEditable());
        optionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());
        optionBusinessRules.validateCorrectOption(existingOption);

        Option option = optionMapper.updateRequestToOption(request);
        option.setQuestion(existingOption.getQuestion());
        optionRepository.save(option);

        UpdateOptionResponse updateOptionResponse = optionMapper.updateOptionToResponse(existingOption);
        updateOptionResponse.setQuestionId(existingOption.getQuestion().getId());

        log.info("Option with ID: {} updated successfully", request.getId());

        return updateOptionResponse;
    }

    @Override
    public GetOptionResponse getOptionByID(Long optionId) {
        log.info("Get request received for option with ID: {}", optionId);


        Option option = optionRepository.findById(optionId).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist)));

        log.info("Successfully retrieved option with ID: {}", optionId);

        return optionMapper.getOptionToResponse(option);
    }

    @Override
    public Page<GetAllOptionResponse> getAllOptions(int page, int size) {
        log.info("Get all options request received for page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Option> optionsPage = optionRepository.findAll(pageable);
        log.info("Options retrieved successfully for page: {}, size: {}", page, size);

        return optionsPage.map(optionMapper::getAllOptionToResponse);
    }

    @Override
    public void deleteOption(Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist)));


        List<Option> options = option.getQuestion().getOptions();

        optionBusinessRules.userAuthorizationCheck(option.getQuestion().getCreatorId());
        questionBusinessRules.checkIfQuestionIsEditable(option.getQuestion().getIsEditable());
        optionBusinessRules.atLeastTwoAnswerChecks(options);
        optionBusinessRules.ensureAtLeastOneCorrectOption(option.getQuestion().getId(), optionId);

        options.removeIf(opt -> opt.getId().equals(optionId));
        optionRepository.deleteById(optionId); //

        log.info("Option with ID: {} deleted successfully", optionId);

    }

    @Override
    public void saveOption(Option option) {
        log.info("Save request received for option: {}", option);

        optionRepository.save(option);

        log.info("Option saved successfully");
    }

    public Option getOption(Long optionId) {
        log.info("Get request received for option entity with ID:  {}", optionId);

        Option option = optionRepository.findById(optionId).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.OptionsErrors.OptionsShouldBeExist)));

        log.info("Successfully retrieved option entity with ID: {}", optionId);

        return option;
    }


}
