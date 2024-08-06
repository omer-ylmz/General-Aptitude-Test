package com.gyt.examservice.entities.concretes;

import com.gyt.examservice.business.dtos.response.get.GetQuestionResponse;
import com.gyt.examservice.entities.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Long organizationId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_STARTED;

    @ElementCollection
    @CollectionTable(name = "exam_question_ids", joinColumns = @JoinColumn(name = "exam_id"))
    @Column(name = "question_id")
    private List<Long> questionIds;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rule> rules;


}
