package com.gyt.examservice.business.dtos.response.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRuleResponse {
    private Long id;
    private String description;
    private Long examId;
}