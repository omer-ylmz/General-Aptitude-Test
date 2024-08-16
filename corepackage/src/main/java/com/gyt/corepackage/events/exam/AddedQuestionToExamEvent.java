package com.gyt.corepackage.events.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddedQuestionToExamEvent {
    private Long examId;
    private Long questionId;
}
