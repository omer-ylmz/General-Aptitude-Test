package com.gyt.examservice.business.dtos.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateQuestionEditableRequest {

    private List<Long> questionIds;
    private boolean isEditable;
}
