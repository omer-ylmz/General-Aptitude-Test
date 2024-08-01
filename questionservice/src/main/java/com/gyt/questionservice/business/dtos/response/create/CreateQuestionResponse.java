package com.gyt.questionservice.business.dtos.response.create;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateQuestionResponse {
    private Long id;
    private String text;
    private Long creatorId;
    private Boolean isEditable;
    private String imageUrl;
    private List<CreateOptionResponse> optionList;
}
