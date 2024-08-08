package com.gyt.searchservice.repository;

import com.gyt.searchservice.models.entities.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, Long> {
}
