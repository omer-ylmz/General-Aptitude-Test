package com.gyt.questionservice.business.concretes;

import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.business.rules.QuestionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.mapper.OptionMapper;
import com.gyt.questionservice.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionManager implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ManagementServiceClient managementServiceClient;
    private final QuestionBusinessRules questionBusinessRules;
    private final OptionBusinessRules optionBusinessRules;
    private final OptionService optionService;

    @Override
    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {
        log.info("Create request received for question with text: {}", request.getText());

        questionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());
        optionBusinessRules.correctOptionCheck(request.getOptionRequestList());

        Question question = QuestionMapper.INSTANCE.createRequestToQuestion(request);

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();
        Long creatorId = questionAddControlByCreatorID(authenticatedUser);
        question.setCreatorId(creatorId);

        question = questionRepository.save(question);

        List<CreateOptionResponse> options = new ArrayList<>();
        for (CreateOptionRequest createOptionRequest : request.getOptionRequestList()) {
            optionBusinessRules.textAndImageValidationRule(createOptionRequest.getText(), createOptionRequest.getImageUrl());

            Option option = OptionMapper.INSTANCE.createRequestToOption(createOptionRequest);
            option.setQuestion(question);

            optionBusinessRules.textAndImageValidationRule(option.getText(), option.getImageUrl());

            optionService.saveOption(option);
            options.add(OptionMapper.INSTANCE.createOptionToResponse(option));
        }

        CreateQuestionResponse questionToResponse = QuestionMapper.INSTANCE.createQuestionToResponse(question);
        questionToResponse.setOptionList(options);

        log.info("Question with text: {} created successfully", request.getText());
        return questionToResponse;
    }

    @Override
    public UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request) {
        log.info("Update request received for question with ID: {}", request.getId());

        questionBusinessRules.questionShouldBeExist(request.getId());
        questionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());

        Question foundQuestion = questionRepository.findById(request.getId()).orElseThrow();

        questionBusinessRules.userAuthorizationCheck(foundQuestion.getCreatorId());

        Question question = QuestionMapper.INSTANCE.updateRequestToQuestion(request);
        question.setCreatorId(foundQuestion.getCreatorId());
        questionRepository.save(question);

        log.info("Question with ID: {} updated successfully", request.getId());

        return QuestionMapper.INSTANCE.updateQuestionToResponse(question);
    }

    @Override
    public GetQuestionResponse getQuestionByID(Long id) {
        log.info("Get request received for question with ID: {}", id);

        questionBusinessRules.questionShouldBeExist(id);

        Question question = questionRepository.findById(id).orElseThrow();
        List<OptionDTO> optionDTOS = question.getOptions().stream()
                .map(OptionMapper.INSTANCE::optionToDTO).toList();

        GetQuestionResponse response = QuestionMapper.INSTANCE.getQuestionToResponse(question);
        response.setOptions(optionDTOS);

        log.info("Question with ID: {} get successfully", id);

        return response;
    }

    @Override
    public Page<GetAllQuestionResponse> getAllQuestion(int page, int size) {
        log.info("Get all questions request received for page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Question> questionsPage = questionRepository.findAll(pageable);
        Page<GetAllQuestionResponse> responsePage = questionsPage.map(
                question -> {
                    List<OptionDTO> optionDTOS = question.getOptions().stream().map(OptionMapper.INSTANCE::optionToDTO).toList();
                    GetAllQuestionResponse response = QuestionMapper.INSTANCE.getAllQuestionToResponse(question);
                    response.setOptions(optionDTOS);
                    return response;
                }
        );
        log.info("Retrieved {} questions for page: {}, size: {}", responsePage.getTotalElements(), page, size);

        return responsePage;
    }

    @Override
    public void deleteQuestionByID(Long id) {
        log.info("Delete request received for question with ID: {}", id);

        questionBusinessRules.questionShouldBeExist(id);

        Question foundQuestion = questionRepository.findById(id).orElseThrow();

        questionBusinessRules.userAuthorizationCheck(foundQuestion.getCreatorId());

        questionRepository.deleteById(id);

        log.info("Question with ID: {} deleted successfully", id);
    }

    @Override
    public CreateOptionResponse addOptionToQuestion(Long questionId, CreateOptionRequest request) {
        log.info("Add option request received for question with ID: {}", questionId);

        questionBusinessRules.questionShouldBeExist(questionId);

        Question question = questionRepository.findById(questionId).orElseThrow();

        optionBusinessRules.upToFiveAnswerChecks(question.getOptions().size());
        optionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());

        Option option = OptionMapper.INSTANCE.createRequestToOption(request);
        option.setQuestion(question);

        optionService.saveOption(option);

        question.getOptions().add(option);
        questionRepository.save(question);

        log.info("Option added to question with ID: {} successfully", questionId);

        return OptionMapper.INSTANCE.createOptionToResponse(option);
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
            log.info("User with ID: {} has organization role", getUserResponse.getId());
            return getUserResponse.getId();
        }
        log.warn("User with ID: {} does have admin role", getUserResponse.getId());
        return null;
    }
}
