package com.gyt.questionservice.business.concretes;

import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.mapper.OptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OptionManager implements OptionService {
    private final OptionRepository optionRepository;
    private final OptionBusinessRules optionBusinessRules;


    @Override
    public UpdateOptionResponse updateOption(UpdateOptionRequest request) {
        optionBusinessRules.optionShouldBeExist(request.getId());

        Option existingOption = optionRepository.findById(request.getId()).orElseThrow();

        optionBusinessRules.userAuthorizationCheck(existingOption.getQuestion().getCreatorId());
        optionBusinessRules.textAndImageValidationRule(request.getText(),request.getImageUrl());
        optionBusinessRules.validateCorrectOption(request);

        Option option = OptionMapper.INSTANCE.updateRequestToOption(request);
        option.setQuestion(existingOption.getQuestion());
        optionRepository.save(option);

        UpdateOptionResponse updateOptionResponse = OptionMapper.INSTANCE.updateOptionToResponse(existingOption);
        updateOptionResponse.setQuestionId(existingOption.getQuestion().getId());
        return updateOptionResponse;
    }

    @Override
    public GetOptionResponse getOptionByID(Long optionId) {
        optionBusinessRules.optionShouldBeExist(optionId);

        Option option = optionRepository.findById(optionId).orElseThrow();

        return OptionMapper.INSTANCE.getOptionToResponse(option);
    }

    @Override
    public Page<GetAllOptionResponse> getAllOptions(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id"));
        Page<Option> optionsPage = optionRepository.findAll(pageable);
        return optionsPage.map(
                OptionMapper.INSTANCE::getAllOptionToResponse
        );
    }

    @Override
    public void deleteOption(Long optionId) {
        optionBusinessRules.optionShouldBeExist(optionId);

        Option option = optionRepository.findById(optionId).orElseThrow();

        optionBusinessRules.userAuthorizationCheck(option.getQuestion().getCreatorId());

        optionRepository.deleteById(optionId);

    }

    @Override
    public void saveOption(Option option) {
        optionRepository.save(option);
    }


}
