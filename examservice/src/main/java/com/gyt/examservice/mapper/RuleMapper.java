package com.gyt.examservice.mapper;

import com.gyt.corepackage.events.rule.RuleEvent;
import com.gyt.examservice.business.dtos.RuleDTO;
import com.gyt.examservice.business.dtos.request.update.UpdateRuleRequest;
import com.gyt.examservice.business.dtos.response.get.GetRuleResponse;
import com.gyt.examservice.business.dtos.response.getAll.GetAllRuleResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateRuleResponse;
import com.gyt.examservice.entities.concretes.Rule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuleMapper {

    Rule requestToRule(RuleDTO ruleDTO);
    RuleDTO ruleToRuleDTO(Rule rule);

    Rule updateRequestToRule(UpdateRuleRequest updateRuleRequest);
    UpdateRuleResponse updateRuleToResponse(Rule rule);

    GetRuleResponse getRuleToResponse(Rule rule);

    GetAllRuleResponse getAllRuleToResponse(Rule rule);

    RuleEvent ruleToRuleEvent(Rule rule);

}
