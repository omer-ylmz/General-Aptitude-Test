package com.gyt.questionservice.business.dtos.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateQuestionRequest {
    private Long id;
    private String text;
    private String imageUrl;
}
