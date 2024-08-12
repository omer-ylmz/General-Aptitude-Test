package com.gyt.examservice.api.controllers;

import com.gyt.examservice.business.abstracts.RuleService;
import com.gyt.examservice.business.dtos.request.update.UpdateRuleRequest;
import com.gyt.examservice.business.dtos.response.get.GetRuleResponse;
import com.gyt.examservice.business.dtos.response.getAll.GetAllRuleResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateRuleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/rule")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @PutMapping("/updateRule")
    public ResponseEntity<UpdateRuleResponse> updateRule(@RequestBody @Valid UpdateRuleRequest updateRuleRequest) {
        log.info("Received request to update rule: {}", updateRuleRequest);
        UpdateRuleResponse updateRuleResponse = ruleService.updateRule(updateRuleRequest);
        log.info("Rule updated successfully with ID: {}", updateRuleResponse.getId());
        return new ResponseEntity<>(updateRuleResponse, HttpStatus.OK);
    }

    @GetMapping("/getRule/{id}")
    public ResponseEntity<GetRuleResponse> getRuleById(@PathVariable Long id) {
        log.info("Received request to get rule by ID: {}", id);
        GetRuleResponse response = ruleService.getRuleById(id);
        log.info("Retrieved rule with ID: {}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllRules")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllRuleResponse> getAllRules(@RequestParam int page, @RequestParam int size) {
        log.info("Received request to get all rules with page: {} and size: {}", page, size);
        Page<GetAllRuleResponse> allRules = ruleService.getAllRules(page, size);
        log.info("Retrieved {} rules", allRules.getTotalElements());
        return allRules;
    }

    @DeleteMapping("/deleteRule/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRuleById(@PathVariable Long id) {
        log.info("Received request to delete rule by ID: {}", id);
        ruleService.deleteRuleById(id);
        log.info("Deleted rule with ID: {}", id);
    }

}
