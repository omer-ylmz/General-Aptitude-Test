package com.gyt.examservice.business.dtos.request.create;

import com.gyt.examservice.business.dtos.RuleDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateExamRequest {
    @NotNull
    private String name;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private List<Long> questionIds;

    @NotNull
    private List<RuleDTO> rules;

}
