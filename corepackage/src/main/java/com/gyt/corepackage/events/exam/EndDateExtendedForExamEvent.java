package com.gyt.corepackage.events.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndDateExtendedForExamEvent {
    private Long examId;
    private LocalDateTime newEndDate;
}
