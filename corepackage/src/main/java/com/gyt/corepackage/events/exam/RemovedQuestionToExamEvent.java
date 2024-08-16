package com.gyt.corepackage.events.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemovedQuestionToExamEvent {
    private Long examId;
    private Long questionId;
}
