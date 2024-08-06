package com.gyt.examservice.business.concretes;

import com.gyt.examservice.business.abstracts.RuleService;
import com.gyt.examservice.dataAccess.abstracts.RuleRepository;
import com.gyt.examservice.entities.concretes.Rule;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RuleManager implements RuleService {
    private final RuleRepository ruleRepository;


    @Override
    public void saveRule(Rule rule) {
        ruleRepository.save(rule);
    }
}
