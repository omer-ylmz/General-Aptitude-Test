package com.gyt.questionservice.business.dtos.response.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateQuestionResponse {
    private Long id;
    private String text;
    private Long creatorId;
    private Boolean isEditable;
    private String imageUrl;
}
