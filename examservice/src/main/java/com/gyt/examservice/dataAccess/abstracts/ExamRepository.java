package com.gyt.examservice.dataAccess.abstracts;

import com.gyt.examservice.entities.concretes.Exam;
import com.gyt.examservice.entities.enums.Status;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Exam e SET e.status = 'IN_PROGRESS' WHERE e.status = 'NOT_STARTED' AND e.startDate <= :now")
    int updateStatusToStarted(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Exam e SET e.status = 'FINISHED' WHERE e.status = 'IN_PROGRESS' AND e.endDate <= :now")
    int updateStatusToFinished(LocalDateTime now);

    @Query("SELECT COUNT(e) > 0 FROM Exam e " +
            "JOIN e.questionIds q " +
            "WHERE q IN :questionIds AND e.status = 'IN_PROGRESS'")
    boolean existsInAnotherInProgressExamWithQuestions(@Param("questionIds") List<Long> questionIds);

    @Query("SELECT e FROM Exam e WHERE e.status = :status")
    List<Exam> findAllByStatus(@Param("status") Status status);

    @Query("SELECT e FROM Exam e LEFT JOIN FETCH e.questionIds WHERE e.id = :id")
    Optional<Exam> findByIdWithQuestionIds(@Param("id") Long id);
}
