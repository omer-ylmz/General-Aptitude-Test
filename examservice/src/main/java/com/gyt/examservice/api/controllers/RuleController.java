package com.gyt.examservice.api.controllers;

import com.gyt.examservice.business.abstracts.RuleService;
import com.gyt.examservice.business.dtos.request.update.UpdateRuleRequest;
import com.gyt.examservice.business.dtos.response.get.GetRuleResponse;
import com.gyt.examservice.business.dtos.response.getAll.GetAllRuleResponse;
import com.gyt.examservice.business.dtos.response.update.UpdateRuleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rule")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @PutMapping("/updateRule")
    public ResponseEntity<UpdateRuleResponse> updateRule(@RequestBody @Valid UpdateRuleRequest updateRuleRequest){
        UpdateRuleResponse updateRuleResponse = ruleService.updateRule(updateRuleRequest);
        return new ResponseEntity<>(updateRuleResponse, HttpStatus.OK);
    }

    @GetMapping("/getRule/{id}")
    public ResponseEntity<GetRuleResponse> getRuleById(@PathVariable Long id){
        GetRuleResponse response = ruleService.getRuleById(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/getAllRules")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllRuleResponse> getAllRules(@RequestParam int page, @RequestParam int size) {
        Page<GetAllRuleResponse> allRules = ruleService.getAllRules(page, size);
        return allRules;
    }

    @DeleteMapping("/deleteRule/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRuleById(@PathVariable Long id){
        ruleService.deleteRuleById(id);
    }

}
