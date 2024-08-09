package com.gyt.examservice.business.abstracts;

import com.gyt.examservice.business.dtos.request.update.UpdateRuleRequest;
import com.gyt.examservice.business.dtos.response.get.GetRuleResponse;

import com.gyt.examservice.business.dtos.response.getAll.GetAllRuleResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateRuleResponse;
import com.gyt.examservice.entities.concretes.Rule;
import org.springframework.data.domain.Page;

public interface RuleService {
    void saveRule(Rule rule);
    UpdateRuleResponse updateRule(UpdateRuleRequest request);
    GetRuleResponse getRuleById(Long id);
    Page<GetAllRuleResponse> getAllRules (int page, int size);
    void deleteRuleById(Long id);


}
