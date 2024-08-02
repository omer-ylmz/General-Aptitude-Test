package com.gyt.questionservice.dataAccess.abstacts;

import com.gyt.questionservice.entities.Question;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
