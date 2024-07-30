package com.gyt.questionservice.business.abstracts;


import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;

public interface OptionService {
    CreateOptionResponse createOption(CreateOptionRequest request);

}
