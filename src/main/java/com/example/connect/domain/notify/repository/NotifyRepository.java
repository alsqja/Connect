package com.example.connect.domain.notify.repository;

import com.example.connect.domain.notify.entity.Notify;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {

    List<Notify> findAllByReceiverIdAndIsReadFalse(Long receiverId);

    @Modifying
    @Query("UPDATE Notify n SET n.isRead = true WHERE n.receiver.id = :userId AND n.isRead = false")
    void markAllAsRead(@Param("userId") Long userId);
}
