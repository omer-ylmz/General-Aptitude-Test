package com.gyt.examservice.dataAccess.abstracts;

import com.gyt.examservice.entities.concretes.Exam;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ExamRepository extends JpaRepository<Exam,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Exam e SET e.status = 'IN_PROGRESS' WHERE e.status = 'NOT_STARTED' AND e.startDate <= :now")
    int updateStatusToStarted(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Exam e SET e.status = 'FINISHED' WHERE e.status = 'IN_PROGRESS' AND e.endDate <= :now")
    int updateStatusToFinished(LocalDateTime now);
}
