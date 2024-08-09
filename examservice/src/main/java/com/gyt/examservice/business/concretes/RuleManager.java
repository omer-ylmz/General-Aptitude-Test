package com.gyt.examservice.business.concretes;

import com.gyt.corepackage.business.abstracts.MessageService;
import com.gyt.corepackage.utils.exceptions.types.BusinessException;
import com.gyt.examservice.api.clients.ManagementServiceClient;
import com.gyt.examservice.business.abstracts.RuleService;
import com.gyt.examservice.business.dtos.request.update.UpdateRuleRequest;
import com.gyt.examservice.business.dtos.response.get.GetRuleResponse;
import com.gyt.examservice.business.dtos.response.get.GetUserResponse;
import com.gyt.examservice.business.dtos.response.getAll.GetAllRuleResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateRuleResponse;
import com.gyt.examservice.business.messages.Messages;
import com.gyt.examservice.business.rules.ExamBusinessRules;
import com.gyt.examservice.dataAccess.abstracts.RuleRepository;
import com.gyt.examservice.entities.concretes.Rule;
import com.gyt.examservice.mapper.RuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RuleManager implements RuleService {
    private final RuleRepository ruleRepository;
    private final MessageService messageService;
    private final ExamBusinessRules examBusinessRules;
    private final ManagementServiceClient managementServiceClient;
    private final RuleMapper ruleMapper;


    @Override
    public void saveRule(Rule rule) {
        ruleRepository.save(rule);
    }

    @Override
    public UpdateRuleResponse updateRule(UpdateRuleRequest request) {
        Rule existingRule = ruleRepository.findById(request.getId()).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.RuleErrors.RuleShouldBeExist)));

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(existingRule.getExam().getOrganizationId(), authenticatedUser);

        Rule rule = ruleMapper.updateRequestToRule(request);
        rule.setExam(existingRule.getExam());
        ruleRepository.save(rule);
        return ruleMapper.updateRuleToResponse(rule);
    }

    @Override
    public GetRuleResponse getRuleById(Long id) {
        Rule rule = ruleRepository.findById(id).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.RuleErrors.RuleShouldBeExist))
        );

        GetRuleResponse response = ruleMapper.getRuleToResponse(rule);
        response.setExamId(rule.getExam().getId());
        return response;
    }

    @Override
    public Page<GetAllRuleResponse> getAllRules(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        Page<Rule> rulePage = ruleRepository.findAll(pageable);

        Page<GetAllRuleResponse> responsePage = rulePage.map(rule -> {
            GetAllRuleResponse response = ruleMapper.getAllRuleToResponse(rule);
            response.setExamId(rule.getExam().getId());
            return response;
        });

        return responsePage;
    }

    @Override
    public void deleteRuleById(Long id) {
        Rule rule = ruleRepository.findById(id).orElseThrow(
                () -> new BusinessException(messageService.getMessage(Messages.RuleErrors.RuleShouldBeExist)));

        GetUserResponse authenticatedUser = managementServiceClient.getAuthenticatedUser();

        examBusinessRules.userAuthorizationCheck(rule.getExam().getOrganizationId(), authenticatedUser);

        ruleRepository.delete(rule);
    }
}
