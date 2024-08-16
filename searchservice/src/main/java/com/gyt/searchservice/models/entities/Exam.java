package com.gyt.searchservice.models.entities;

import com.gyt.corepackage.events.rule.RuleEvent;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "exams")
public class Exam {

    @Id
    private Long id;

    @Field(name = "name")
    private String name;

    @Field(name = "start_date")
    private LocalDateTime startDate;

    @Field(name = "end_date")
    private LocalDateTime endDate;

    @Field(name = "rules")
    private List<RuleEvent> rules;

    @Field(name = "question_size")
    private Integer questionSize;
}
