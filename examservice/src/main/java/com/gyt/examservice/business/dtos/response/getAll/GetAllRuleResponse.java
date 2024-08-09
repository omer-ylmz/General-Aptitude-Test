package com.gyt.examservice.business.dtos.response.getAll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllRuleResponse {
    private Long id;
    private String description;
    private Long examId;
}
