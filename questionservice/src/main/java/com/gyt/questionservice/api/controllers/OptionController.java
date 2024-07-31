package com.gyt.questionservice.api.controllers;

import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/option")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    @PostMapping("/createoption")
    public ResponseEntity<CreateOptionResponse> createOption(@RequestBody CreateOptionRequest request){
        return new  ResponseEntity<>(optionService.createOption(request), HttpStatus.CREATED);
    }

    @PutMapping("/updateoption")
    public ResponseEntity<UpdateOptionResponse> updateOption(@RequestBody UpdateOptionRequest request){
        return new  ResponseEntity<>(optionService.updateOption(request), HttpStatus.OK);
    }

    @GetMapping("/getoption/{id}")
    public ResponseEntity<GetOptionResponse> getOption(@PathVariable Long id){
        return new ResponseEntity<>(optionService.getOptionByID(id), HttpStatus.OK);
    }

    @GetMapping("/getall")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllOptionResponse> getAllOption(@RequestParam int page, @RequestParam int size){
        return optionService.getAllOptions(page,size);
    }

    @DeleteMapping("/deleteoption/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOption(@PathVariable Long id){
        optionService.deleteOption(id);
    }

}
