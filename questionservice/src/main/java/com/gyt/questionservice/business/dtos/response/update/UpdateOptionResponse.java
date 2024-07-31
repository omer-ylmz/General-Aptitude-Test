package com.gyt.questionservice.business.dtos.response.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOptionResponse {
    private Long id;
    private String text;
    private Boolean isCorrect;
    private String imageUrl;
    private Long questionId;
}
