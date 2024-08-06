package com.gyt.questionservice.entities;

import com.gyt.corepackage.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String text;

    @Column
    private Long creatorId;

    @Column(nullable = false)
    private Boolean isEditable = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Option> options;

    @Column
    private String imageUrl;

}