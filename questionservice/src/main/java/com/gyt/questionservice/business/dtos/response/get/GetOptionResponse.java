package com.gyt.questionservice.business.dtos.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetOptionResponse {
    private Long id;
    private String text;
    private Boolean isCorrect;
    private Long questionId;
    private String imageUrl;
}
