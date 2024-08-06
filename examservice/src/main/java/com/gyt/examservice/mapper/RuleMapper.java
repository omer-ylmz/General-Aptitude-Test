package com.gyt.examservice.mapper;

import com.gyt.examservice.business.dtos.RuleDTO;
import com.gyt.examservice.entities.concretes.Rule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuleMapper {



    Rule requestToRule(RuleDTO ruleDTO);
    RuleDTO ruleToRuleDTO(Rule rule);

}
