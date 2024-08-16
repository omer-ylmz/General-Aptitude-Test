package com.gyt.corepackage.events.exam;

import com.gyt.corepackage.events.rule.RuleEvent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatedExamEvent {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<RuleEvent> rules;
    private Integer questionSize;

}
