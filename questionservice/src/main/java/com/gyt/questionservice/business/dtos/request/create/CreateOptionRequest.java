package com.gyt.questionservice.business.dtos.request.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOptionRequest {
    private String text;
    private Boolean isCorrect;
    private String imageUrl;

}
