package com.gyt.examservice.dataAccess.abstracts;

import com.gyt.examservice.entities.concretes.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}
