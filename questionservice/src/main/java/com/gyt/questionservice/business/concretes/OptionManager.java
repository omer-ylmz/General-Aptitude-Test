package com.gyt.questionservice.business.concretes;

import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.dataAccess.abstacts.OptionRepository;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.mapper.OptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptionManager implements OptionService {
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository; // TODO: 30.07.2024 kalkacak buradan

    @Override
    public CreateOptionResponse createOption(CreateOptionRequest request) {

        Option option = OptionMapper.INSTANCE.createRequestToOption(request);
        Question question = questionRepository.findById(request.getQuestionId()).orElseThrow();
        option.setQuestion(question);
        optionRepository.save(option);
        CreateOptionResponse createOptionResponse = OptionMapper.INSTANCE.createOptionToResponse(option);
        createOptionResponse.setQuestionId(request.getQuestionId());
        return createOptionResponse;
    }



}
