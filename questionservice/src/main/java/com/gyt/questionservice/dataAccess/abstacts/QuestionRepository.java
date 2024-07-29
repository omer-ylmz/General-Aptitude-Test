package com.gyt.questionservice.dataAccess.abstacts;

import com.gyt.questionservice.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
