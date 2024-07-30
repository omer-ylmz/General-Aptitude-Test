package com.gyt.questionservice.api.controllers;

import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.create.CreateQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/option")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    @PostMapping
    @ResponseStatus
    public ResponseEntity<CreateOptionResponse> createOption(@RequestBody CreateOptionRequest request){
        return new  ResponseEntity<>(optionService.createOption(request), HttpStatus.OK);
    }
}
