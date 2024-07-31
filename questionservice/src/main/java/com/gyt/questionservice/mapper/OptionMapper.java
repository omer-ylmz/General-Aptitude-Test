package com.gyt.questionservice.mapper;

import com.gyt.questionservice.business.dtos.request.create.CreateOptionRequest;
import com.gyt.questionservice.business.dtos.request.update.UpdateOptionRequest;
import com.gyt.questionservice.business.dtos.response.create.CreateOptionResponse;
import com.gyt.questionservice.business.dtos.response.get.GetOptionResponse;
import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import com.gyt.questionservice.business.dtos.response.getAll.GetAllOptionResponse;
import com.gyt.questionservice.business.dtos.response.update.UpdateOptionResponse;
import com.gyt.questionservice.entities.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OptionMapper {
    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    OptionDTO optionToDTO(Option option);

    Option createRequestToOption(CreateOptionRequest request);
    CreateOptionResponse createOptionToResponse(Option option);

    Option updateRequestToOption(UpdateOptionRequest request);
    UpdateOptionResponse updateOptionToResponse(Option option);

    @Mapping(source = "question.id", target = "questionId")
    GetOptionResponse getOptionToResponse(Option option);

    @Mapping(source = "question.id", target = "questionId")
    GetAllOptionResponse getAllOptionToResponse(Option option);
}
