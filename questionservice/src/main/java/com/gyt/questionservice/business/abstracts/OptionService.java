package com.gyt.questionservice.business.abstracts;


import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import com.gyt.questionservice.entities.Option;
import org.springframework.data.domain.Page;

public interface OptionService {
    UpdateOptionResponse updateOption(UpdateOptionRequest request);
    GetOptionResponse getOptionByID(Long optionId);
    Page<GetAllOptionResponse> getAllOptions(int page, int size);
    void deleteOption(Long optionId);
    void saveOption(Option option);

}
