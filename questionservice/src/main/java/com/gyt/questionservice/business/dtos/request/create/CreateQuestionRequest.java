package com.gyt.questionservice.business.dtos.request.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateQuestionRequest {
    private String text;
    private String imageUrl;

}
