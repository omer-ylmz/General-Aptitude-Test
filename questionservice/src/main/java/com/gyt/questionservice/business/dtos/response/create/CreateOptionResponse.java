package com.gyt.questionservice.business.dtos.response.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOptionResponse {
    private Long id; // Veritabanı tarafından atanmış ID
    private String text;
    private Boolean isCorrect;
    private Long questionId; // İlişkilendirilen soru ID'si
}
