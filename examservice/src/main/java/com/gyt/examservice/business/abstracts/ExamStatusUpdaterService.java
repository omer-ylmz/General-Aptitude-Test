package com.gyt.examservice.business.abstracts;

import com.gyt.examservice.dataAccess.abstracts.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExamStatusUpdaterService {
    private final ExamRepository examRepository;

    @Scheduled(fixedRate = 60000)
    public void updateExamStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // Başlamamış sınavları güncelle
        examRepository.updateStatusToStarted(now);

        // Devam eden sınavları güncelle
        examRepository.updateStatusToFinished(now);

    }

}
