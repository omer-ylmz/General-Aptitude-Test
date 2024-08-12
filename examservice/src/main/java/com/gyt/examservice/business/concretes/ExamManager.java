package com.gyt.examservice.business.concretes;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.examservice.api.clients.ManagementServiceClient;
import com.gyt.examservice.business.abstracts.ExamService;
import com.gyt.examservice.business.dtos.RuleDTO;
import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.request.update.UpdateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.business.dtos.response.get.OptionDTO;
import com.gyt.examservice.business.dtos.response.getAll.GetAllExamResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateExamResponse;
import com.gyt.examservice.business.messages.Messages;
import com.gyt.examservice.business.rules.ExamBusinessRules;
import com.gyt.examservice.dataAccess.abstracts.ExamRepository;
import com.gyt.examservice.entities.concretes.Exam;
import com.gyt.examservice.entities.concretes.Rule;
import com.gyt.examservice.mapper.ExamMapper;
import com.gyt.examservice.mapper.RuleMapper;
import com.gyt.questionservice.GrpcGetQuestionRequest;
import com.gyt.questionservice.GrpcGetQuestionResponse;
import com.gyt.questionservice.QuestionServiceGrpc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExamManager implements ExamService {
    private final ExamRepository examRepository;
    private final ManagementServiceClient managementServiceClient;
    private final QuestionServiceGrpc.QuestionServiceBlockingStub questionServiceBlockingStub;
    private final ExamMapper examMapper;
    private final RuleMapper ruleMapper;
    private final ExamBusinessRules examBusinessRules;
    private final MessageService messageService;


    @Override
    @Transactional
    public CreateExamResponse createExam(CreateExamRequest createExamRequest) {
        //Todo grpcden giden istek de soru yoksa hata fırlatmalı
        log.info("Creating exam with request: {}", createExamRequest);

        examBusinessRules.validateExamDates(createExamRequest.getStartDate(), createExamRequest.getEndDate());
        examBusinessRules.validateUniqueQuestions(createExamRequest.getQuestionIds());

        GetUserResponse getUserResponse = managementServiceClient.getAuthenticatedUser();

        Exam exam = examMapper.createRequestToExam(createExamRequest);
        exam.setOrganizationId(getUserResponse.getId());

        List<RuleDTO> createRuleResponses = new ArrayList<>();
        List<Rule> rules = new ArrayList<>();
        for (RuleDTO ruleDTO : createExamRequest.getRules()) {
            Rule rule = ruleMapper.requestToRule(ruleDTO);
            rule.setExam(exam);
            rules.add(rule);
            RuleDTO ruleToResponse = ruleMapper.ruleToRuleDTO(rule);
            createRuleResponses.add(ruleToResponse);
        }
        exam.setRules(rules);

        examRepository.save(exam);


        CreateExamResponse createExamResponse = examMapper.createExamToResponse(exam);
        createExamResponse.setQuestions(fetchAndMapQuestions(createExamRequest.getQuestionIds()));
        createExamResponse.setRules(createRuleResponses);

        log.info("Created exam with ID: {}", createExamResponse.getId());

        return createExamResponse;
    }

    @Override
    @Transactional
    public UpdateExamResponse updateExam(UpdateExamRequest updateExamRequest) {
        log.info("Updating exam with request: {}", updateExamRequest);

        examBusinessRules.validateExamDates(updateExamRequest.getStartDate(), updateExamRequest.getEndDate());
        examBusinessRules.validateUniqueQuestions(updateExamRequest.getQuestionIds());


        Exam existingExam = examRepository.findById(updateExamRequest.getId()).orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        examBusinessRules.checkIfExamCanBeModified(existingExam.getStatus());

        GetUserResponse getUserResponse = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(existingExam.getOrganizationId(), getUserResponse);

        Exam exam = examMapper.updateRequestToExam(updateExamRequest);
        exam.setRules(existingExam.getRules());
        exam.setOrganizationId(exam.getOrganizationId());
        examRepository.save(exam);

        UpdateExamResponse updateExamResponse = examMapper.updateExamToResponse(existingExam);
        updateExamResponse.setQuestions(fetchAndMapQuestions(updateExamRequest.getQuestionIds()));

        log.info("Updated exam with ID: {}", updateExamResponse.getId());

        return updateExamResponse;
    }

    public GetExamResponse getExamById(Long examId) {
        log.info("Fetching exam by ID: {}", examId);

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        List<RuleDTO> ruleDTOs = exam.getRules().stream().map(ruleMapper::ruleToRuleDTO).collect(Collectors.toList());

        List<GetQuestionResponse> questionResponses = fetchAndMapQuestions(exam.getQuestionIds());

        GetExamResponse getExamResponse = examMapper.getExamToResponse(exam);
        getExamResponse.setQuestions(questionResponses);
        getExamResponse.setRules(ruleDTOs);

        log.info("Fetched exam with ID: {}", examId);

        return getExamResponse;
    }

    @Override
    public Page<GetAllExamResponse> getAllExam(int page, int size) {
        log.info("Fetching all exams with page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Exam> examPage = examRepository.findAll(pageable);

        Page<GetAllExamResponse> responsePage = examPage.map(exam -> {
            GetAllExamResponse response = examMapper.getAllExamToResponse(exam);

            List<GetQuestionResponse> questionResponses = fetchAndMapQuestions(exam.getQuestionIds());
            response.setQuestions(questionResponses);

            List<RuleDTO> ruleDTOs = exam.getRules().stream()
                    .map(ruleMapper::ruleToRuleDTO)
                    .collect(Collectors.toList());
            response.setRules(ruleDTOs);

            return response;
        });

        log.info("Fetched {} exams", responsePage.getTotalElements());

        return responsePage;
    }

    @Override
    public void deleteExamById(Long id) {
        log.info("Deleting exam by ID: {}", id);

        Exam exam = examRepository.findById(id).orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(exam.getOrganizationId(), authenticatedUser);
        examBusinessRules.checkIfExamCanBeModified(exam.getStatus());

        examRepository.deleteById(id);

        log.info("Deleted exam with ID: {}", id);
    }

    @Override
    @Transactional
    public void addQuestionToExam(Long examId, Long questionId) {
        log.info("Adding question ID: {} to exam ID: {}", questionId, examId);

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(exam.getOrganizationId(), authenticatedUser);
        examBusinessRules.checkIfExamCanBeModified(exam.getStatus());
        examBusinessRules.checkIfQuestionAlreadyExistsInExam(exam, questionId);

        exam.getQuestionIds().add(questionId);
        examRepository.save(exam);

        log.info("Added question ID: {} to exam ID: {}", questionId, examId);
    }

    @Override
    public void removeQuestionFromExam(Long examId, Long questionId) {
        log.info("Removing question ID: {} from exam ID: {}", questionId, examId);

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(exam.getOrganizationId(), authenticatedUser);
        examBusinessRules.checkIfExamCanBeModified(exam.getStatus());
        examBusinessRules.checkIfQuestionExistsInExam(exam.getQuestionIds(), questionId);
        examBusinessRules.checkIfLastQuestionInExam(exam);

        exam.getQuestionIds().remove(questionId);
        examRepository.save(exam);

        log.info("Removed question ID: {} from exam ID: {}", questionId, examId);
    }

    @Override
    public void extendExamEndDate(Long examId, LocalDateTime newEndDate) {
        log.info("Extending end date of exam ID: {} to new end date: {}", examId, newEndDate);

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new BusinessException(messageService.getMessage(Messages.ExamErrors.ExamShouldBeExist)));

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(exam.getOrganizationId(), authenticatedUser);
        examBusinessRules.checkIfExamIsInProgress(exam.getStatus());

        exam.setEndDate(newEndDate);
        examRepository.save(exam);

        log.info("Extended end date of exam ID: {} to new end date: {}", examId, newEndDate);
    }


    private List<GetQuestionResponse> fetchAndMapQuestions(List<Long> questionIds) {
        log.info("Fetching questions for IDs: {}", questionIds);

        List<GetQuestionResponse> getQuestionResponses = new ArrayList<>();
        for (long questionId : questionIds) {
            GrpcGetQuestionRequest request = GrpcGetQuestionRequest.newBuilder().setId(questionId).build();
            GrpcGetQuestionResponse response = questionServiceBlockingStub.getQuestionByID(request);

            GetQuestionResponse getQuestionResponse = examMapper.grpcGetQuestionResponseToResponse(response);

            List<OptionDTO> optionDTOs = response.getOptionsList().stream().map(option -> new OptionDTO(option.getId(), option.getText(), option.getImageUrl())).collect(Collectors.toList());

            getQuestionResponse.setOptions(optionDTOs);
            getQuestionResponses.add(getQuestionResponse);
        }
        log.info("Fetched {} questions", getQuestionResponses.size());

        return getQuestionResponses;
    }
}
