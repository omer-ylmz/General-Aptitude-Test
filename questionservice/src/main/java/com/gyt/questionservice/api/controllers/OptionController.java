package com.gyt.questionservice.api.controllers;

import com.gyt.questionservice.business.abstracts.OptionService;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/option")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;


    @PutMapping("/updateoption")
    public ResponseEntity<UpdateOptionResponse> updateOption(@RequestBody @Valid UpdateOptionRequest request) {
        log.info("Update request received for option with ID: {}", request.getId());
        UpdateOptionResponse response = optionService.updateOption(request);
        log.info("Option with ID: {} updated successfully", response.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getoption/{id}")
    public ResponseEntity<GetOptionResponse> getOption(@PathVariable Long id) {
        log.info("Get request received for option with ID: {}", id);
        GetOptionResponse response = optionService.getOptionByID(id);
        log.info("Option with ID: {} retrieved successfully", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getall")
    @ResponseStatus(HttpStatus.OK)
    public Page<GetAllOptionResponse> getAllOption(@RequestParam int page, @RequestParam int size) {
        log.info("Get all options request received for page: {}, size: {}", page, size);
        Page<GetAllOptionResponse> allOptions = optionService.getAllOptions(page, size);
        log.info("Options retrieved successfully for page: {}, size: {}", page, size);
        return allOptions;
    }

    @DeleteMapping("/deleteoption/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOption(@PathVariable Long id) {
        log.info("Delete request received for option with ID: {}", id);
        optionService.deleteOption(id);
        log.info("Option with ID: {} deleted successfully", id);
    }

}
