package com.gyt.questionservice.business.concretes;

import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import com.gyt.questionservice.business.rules.QuestionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
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


@RequiredArgsConstructor
@Service
public class QuestionManager implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ManagementServiceClient managementServiceClient;
    private final QuestionBusinessRules questionBusinessRules;

    @Override
    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {
        questionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());
        Question question = QuestionMapper.INSTANCE.createRequestToQuestion(request);
        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();
        Long creatorId = questionAddControlByCreatorID(authenticatedUser);
        question.setCreatorId(creatorId);
        questionRepository.save(question);
        return QuestionMapper.INSTANCE.createQuestionToResponse(question);
    }

    @Override
    public UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request) {
        questionBusinessRules.questionShouldBeExist(request.getId());
        questionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());
        Question foundQuestion = questionRepository.findById(request.getId()).orElseThrow();
        questionBusinessRules.userAuthorizationCheck(foundQuestion.getCreatorId());
        Question question = QuestionMapper.INSTANCE.updateRequestToQuestion(request);
        question.setCreatorId(foundQuestion.getCreatorId());
        questionRepository.save(question);
        return QuestionMapper.INSTANCE.updateQuestionToResponse(question);
    }

    @Override
    public GetQuestionResponse getQuestionByID(Long id) {
        questionBusinessRules.questionShouldBeExist(id);
        Question question = questionRepository.findById(id).orElseThrow();
        List<OptionDTO> optionDTOS = question.getOptions().stream()
                .map(OptionMapper.INSTANCE::optionToDTO).toList();
        GetQuestionResponse response = QuestionMapper.INSTANCE.getQuestionToResponse(question);
        response.setOptions(optionDTOS);
        return response;
    }

    @Override
    public Page<GetAllQuestionResponse> getAllQuestion(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Question> questionsPage = questionRepository.findAll(pageable);
        return questionsPage.map(
                question -> {
                    List<OptionDTO> optionDTOS = question.getOptions().stream().map(OptionMapper.INSTANCE::optionToDTO).toList();
                    GetAllQuestionResponse response = QuestionMapper.INSTANCE.getAllQuestionToResponse(question);
                    response.setOptions(optionDTOS);
                    return response;
                }
        );
    }

    @Override
    public void deleteQuestionByID(Long id) {
        questionBusinessRules.questionShouldBeExist(id);
        Question foundQuestion = questionRepository.findById(id).orElseThrow();
        questionBusinessRules.userAuthorizationCheck(foundQuestion.getCreatorId());
        questionRepository.deleteById(id);
    }

    public Long questionAddControlByCreatorID(GetUserResponse getUserResponse) {
        boolean hasOrganizationRole = false;

        for (String role : getUserResponse.getRoles()) {
            if (role.equals("organization")) {
                hasOrganizationRole = true;
                break;
            }
        }
        if (hasOrganizationRole) {
            return getUserResponse.getId();
        }
        return null;
    }


}
