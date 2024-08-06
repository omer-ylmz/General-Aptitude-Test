package com.gyt.examservice.business.dtos.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetQuestionResponse {
    private Long id;
    private String text;
    private Long creatorId;
    private Boolean isEditable;
    private String imageUrl;
    private List<OptionDTO> options;
}
