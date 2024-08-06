package com.gyt.examservice.business.dtos.response.create;

import com.gyt.examservice.business.dtos.RuleDTO;
import com.gyt.examservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.examservice.entities.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateExamResponse {
    private Long id;
    private String name;
    private String organizationId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
    private List<GetQuestionResponse> questions;
    private List<RuleDTO> rules;
}
