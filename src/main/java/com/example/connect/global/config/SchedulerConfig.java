package com.example.connect.global.config;

import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.pointuse.entity.PointUse;
import com.example.connect.domain.pointuse.repository.PointUseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {
    private final PointUseRepository pointUseRepository;
    private final PointService pointService;

    @Scheduled(cron = "0 0 0 * * *")
    public void RemoveExpirationPoint() {
        LocalDateTime now = LocalDateTime.now().minusYears(1);
        List<PointUse> pointUseList = pointUseRepository.findExpiredPoints(now);

        pointService.expiredPoint(pointUseList);
    }
}
