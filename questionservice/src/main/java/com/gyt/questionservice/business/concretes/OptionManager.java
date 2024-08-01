package com.gyt.questionservice.business.concretes;

import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.mapper.OptionMapper;
import com.gyt.questionservice.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OptionManager implements OptionService {
    private final OptionRepository optionRepository;
    private final OptionBusinessRules optionBusinessRules;


    @Override
    public CreateOptionResponse createOption(CreateOptionRequest request) {
//        optionBusinessRules.textAndImageValidationRule(request.getText(),request.getImageUrl());
//        Option option = OptionMapper.INSTANCE.createRequestToOption(request);
//        validateAndSaveOption(option, request.getQuestionId(), request.getIsCorrect());
//        CreateOptionResponse createOptionResponse = OptionMapper.INSTANCE.createOptionToResponse(option);
//        createOptionResponse.setQuestionId(request.getQuestionId());
//        return createOptionResponse;
        return null;
    }




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
//        optionBusinessRules.optionShouldBeExist(optionId);
//        Option option = optionRepository.findById(optionId).orElseThrow();
//        return OptionMapper.INSTANCE.getOptionToResponse(option);
        return null;
    }

    @Override
    public Page<GetAllOptionResponse> getAllOptions(int page, int size) {
//        Pageable pageable = PageRequest.of(page,size, Sort.by("id"));
//        Page<Option> optionsPage = optionRepository.findAll(pageable);
//        return optionsPage.map(
//                OptionMapper.INSTANCE::getAllOptionToResponse
//        );
        return null;
    }

    @Override
    public void deleteOption(Long optionId) {
//        optionBusinessRules.optionShouldBeExist(optionId);
//        GetOptionResponse getOptionResponse = getOptionByID(optionId);
//        GetQuestionResponse getQuestionResponse = questionService.getQuestionByID(getOptionResponse.getQuestionId());
//        optionBusinessRules.userAuthorizationCheck(getQuestionResponse.getCreatorId());
//        optionRepository.deleteById(optionId);

    }



    private void validateAndSaveOption(Option option, Long questionId, Boolean isCorrect) {
//        GetQuestionResponse getQuestionResponse = questionService.getQuestionByID(questionId);
//        optionBusinessRules.userAuthorizationCheck(getQuestionResponse.getCreatorId());
//        optionBusinessRules.upToFiveAnswerChecks(getQuestionResponse.getOptions());
//        optionBusinessRules.correctOptionCheck(getQuestionResponse.getOptions(), isCorrect);
//        Question question = QuestionMapper.INSTANCE.getResponseToQuestion(getQuestionResponse);
//        option.setQuestion(question);
//        optionRepository.save(option);
    }

    @Override
    public Option getOption(Long id){
        optionBusinessRules.optionShouldBeExist(id);
        Optional<Option> option = optionRepository.findById(id);
        return option.get();
    }

    @Override
    public void saveOption(Option option) {
        optionRepository.save(option);
    }


}
