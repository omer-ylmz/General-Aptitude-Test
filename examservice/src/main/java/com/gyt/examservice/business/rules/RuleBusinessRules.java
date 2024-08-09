package com.gyt.examservice.business.rules;

import com.gyt.corepackage.business.abstracts.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RuleBusinessRules {
    private final MessageService messageService;


}
