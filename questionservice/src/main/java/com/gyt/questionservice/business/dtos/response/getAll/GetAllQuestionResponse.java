package com.gyt.questionservice.business.dtos.response.getAll;

import com.gyt.questionservice.business.dtos.response.get.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllQuestionResponse {
    private Long id;
    private String text;
    private Long creatorId;
    private Boolean isEditable;
    private String imageUrl;
    private List<OptionDTO> options;
}
