package com.gyt.examservice.business.concretes;

import com.gyt.examservice.api.clients.ManagementServiceClient;
import com.gyt.examservice.api.clients.QuestionServiceClient;
import com.gyt.examservice.business.abstracts.ExamService;
import com.gyt.examservice.business.dtos.RuleDTO;
import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;

import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import com.gyt.examservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.dataAccess.abstracts.ExamRepository;
import com.gyt.examservice.entities.concretes.Exam;
import com.gyt.examservice.entities.concretes.Rule;
import com.gyt.examservice.mapper.ExamMapper;
import com.gyt.examservice.mapper.RuleMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExamManager implements ExamService {
    private final ExamRepository examRepository;
    private final ManagementServiceClient managementServiceClient;
    private final QuestionServiceClient questionServiceClient;
    private final ExamMapper examMapper;
    private final RuleMapper ruleMapper;


    @Override
    @Transactional
    public CreateExamResponse createExam(CreateExamRequest createExamRequest) {
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

        List<GetQuestionResponse> getQuestionResponses = new ArrayList<>();
        for (long questionId : createExamRequest.getQuestionIds()) {
             GetQuestionResponse response = questionServiceClient.getQuestionById(questionId);
             getQuestionResponses.add(response);
        }

        CreateExamResponse createExamResponse = examMapper.createExamToResponse(exam);
        createExamResponse.setQuestions(getQuestionResponses);
        createExamResponse.setRules(createRuleResponses);

        return createExamResponse;
    }


}
