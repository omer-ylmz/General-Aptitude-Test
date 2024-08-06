package com.gyt.examservice.mapper;

import com.gyt.examservice.business.dtos.request.create.CreateExamRequest;
import com.gyt.examservice.business.dtos.response.create.CreateExamResponse;
import com.gyt.examservice.entities.concretes.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    Exam createRequestToExam(CreateExamRequest request);
    CreateExamResponse createExamToResponse(Exam exam);
}
