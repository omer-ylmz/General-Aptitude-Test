package com.gyt.questionservice.business.concretes;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.events.question.CreatedQuestionEvent;
import com.gyt.corepackage.events.question.DeletedQuestionEvent;
import com.gyt.corepackage.events.question.UpdatedQuestionEvent;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.questionservice.api.clients.ManagementServiceClient;
import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.abstracts.QuestionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.create.CreateQuestionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionEditableRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateQuestionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetUserResponse;
import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllQuestionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateQuestionResponse;
import com.gyt.questionservice.business.messages.Messages;
import com.gyt.questionservice.business.rules.OptionBusinessRules;
import com.gyt.questionservice.business.rules.QuestionBusinessRules;
import com.gyt.questionservice.dataAccess.abstacts.QuestionRepository;
import com.gyt.questionservice.entities.Option;
import com.gyt.questionservice.entities.Question;
import com.gyt.questionservice.kafka.producer.QuestionProducer;
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
    private final OptionMapper optionMapper;
    private final QuestionProducer questionProducer;
    private final QuestionMapper questionMapper;
    private final MessageService messageService;


    @Override
    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {
        log.info("Create request received for question with text: {}", request.getText());

        questionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());
        optionBusinessRules.correctOptionCheck(request.getOptionRequestList());

        Question question = questionMapper.createRequestToQuestion(request);

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();
        Long creatorId = questionAddControlByCreatorID(authenticatedUser);
        question.setCreatorId(creatorId);

        question = questionRepository.save(question);

        List<CreateOptionResponse> options = new ArrayList<>();
        for (CreateOptionRequest createOptionRequest : request.getOptionRequestList()) {
            optionBusinessRules.textAndImageValidationRule(createOptionRequest.getText(), createOptionRequest.getImageUrl());

            Option option = optionMapper.createRequestToOption(createOptionRequest);
            option.setQuestion(question);

            optionBusinessRules.textAndImageValidationRule(option.getText(), option.getImageUrl());

            optionService.saveOption(option);
            options.add(optionMapper.createOptionToResponse(option));
        }

        CreateQuestionResponse questionToResponse = questionMapper.createQuestionToResponse(question);
        questionToResponse.setOptionList(options);
        sendCreatedQuestionToSearchService(question);

        log.info("Question with text: {} created successfully", request.getText());
        return questionToResponse;
    }

    @Override
    public UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request) {
        log.info("Update request received for question with ID: {}", request.getId());

        Question foundQuestion = questionRepository.findById(request.getId()).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.QuestionErrors.QuestionShouldBeExist)));

        questionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());
        questionBusinessRules.userAuthorizationCheck(foundQuestion.getCreatorId());
        questionBusinessRules.checkIfQuestionIsEditable(foundQuestion.getIsEditable());

        Question question = questionMapper.updateRequestToQuestion(request);
        question.setCreatorId(foundQuestion.getCreatorId());
        questionRepository.save(question);

        sendUpdatedQuestionToSearchService(question);

        log.info("Question with ID: {} updated successfully", request.getId());

        return questionMapper.updateQuestionToResponse(question);
    }

    @Override
    public GetQuestionResponse getQuestionByID(Long id) {
        log.info("Get request received for question with ID: {}", id);

        Question question = questionRepository.findById(id).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.QuestionErrors.QuestionShouldBeExist)));

        List<OptionDTO> optionDTOS = question.getOptions().stream()
                .map(optionMapper::optionToDTO).toList();

        GetQuestionResponse response = questionMapper.getQuestionToResponse(question);
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
                    List<OptionDTO> optionDTOS = question.getOptions().stream().map(optionMapper::optionToDTO).toList();
                    GetAllQuestionResponse response = questionMapper.getAllQuestionToResponse(question);
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

        Question foundQuestion = questionRepository.findById(id).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.QuestionErrors.QuestionShouldBeExist)));

        questionBusinessRules.userAuthorizationCheck(foundQuestion.getCreatorId());
        questionBusinessRules.checkIfQuestionIsEditable(foundQuestion.getIsEditable());

        questionRepository.deleteById(id);

        sendDeletedQuestionToSearchService(foundQuestion);

        log.info("Question with ID: {} deleted successfully", id);
    }

    @Override
    public CreateOptionResponse addOptionToQuestion(Long questionId, CreateOptionRequest request) {
        log.info("Add option request received for question with ID: {}", questionId);

        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.QuestionErrors.QuestionShouldBeExist)));

        questionBusinessRules.checkIfQuestionIsEditable(question.getIsEditable());
        optionBusinessRules.upToFiveAnswerChecks(question.getOptions().size());
        optionBusinessRules.textAndImageValidationRule(request.getText(), request.getImageUrl());

        Option option = optionMapper.createRequestToOption(request);
        option.setQuestion(question);

        optionService.saveOption(option);

        question.getOptions().add(option);
        questionRepository.save(question);

        log.info("Option added to question with ID: {} successfully", questionId);

        return optionMapper.createOptionToResponse(option);
    }

    @Override
    public void updateQuestionsEditableStatus(UpdateQuestionEditableRequest request) {
        log.info("Updating question editable status for request: {}", request);

        List<Question> questions = questionRepository.findAllById(request.getQuestionIds());
        for (Question question : questions) {
            question.setIsEditable(request.isEditable());
        }
        questionRepository.saveAll(questions);

        log.info("Updated {} questions with editable status: {}", questions.size(), request.isEditable());
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

    private void sendCreatedQuestionToSearchService(Question question) {
        CreatedQuestionEvent createdQuestionEvent = questionMapper.questionToCreatedQuestionEvent(question);
        questionProducer.sendQuestionForCreate(createdQuestionEvent);
    }

    private void sendUpdatedQuestionToSearchService(Question question) {
        UpdatedQuestionEvent updatedQuestionEvent = questionMapper.questionToUpdatedQuestionEvent(question);
        questionProducer.sendQuestionForUpdate(updatedQuestionEvent);
    }

    private void sendDeletedQuestionToSearchService(Question question) {
        DeletedQuestionEvent deletedQuestionEvent = questionMapper.questionToDeletedQuestionEvent(question);
        questionProducer.sendQuestionForDelete(deletedQuestionEvent);
    }
}
