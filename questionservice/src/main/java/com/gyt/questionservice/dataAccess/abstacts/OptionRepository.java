package com.gyt.questionservice.dataAccess.abstacts;

import com.gyt.questionservice.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
