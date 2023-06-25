package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.message.MessageRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRateRepository extends JpaRepository<MessageRate, Long> {
    @Query(value = "SELECT AVG(rate) FROM MessageRate WHERE messageId = ?1", nativeQuery = true)
    Optional<Double> getAverageRateByMessageId(Long messageId);

    Double getRateByUserId(Long userId, Long messageId);
}
