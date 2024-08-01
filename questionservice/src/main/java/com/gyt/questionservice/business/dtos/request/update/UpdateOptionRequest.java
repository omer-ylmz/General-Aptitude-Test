package com.gyt.questionservice.business.dtos.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateOptionRequest {
    private Long id;
    private String text;
    private Boolean isCorrect;
    private String imageUrl;
}
