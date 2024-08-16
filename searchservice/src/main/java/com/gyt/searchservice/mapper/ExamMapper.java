package com.gyt.searchservice.mapper;

import com.gyt.corepackage.events.exam.CreatedExamEvent;
import com.gyt.corepackage.events.exam.UpdatedExamEvent;
import com.gyt.searchservice.models.entities.Exam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    Exam createdExamEventToExam(CreatedExamEvent event);

    Exam updatedExamEventToExam(UpdatedExamEvent event);


}
