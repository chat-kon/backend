package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.message.MessageRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRateRepository extends JpaRepository<MessageRate, Long> {
    @Query(value = "SELECT AVG(m.rate) FROM MessageRate m WHERE m.id = ?1")
    Optional<Double> getAverageRateByMessageId(Long messageId);

    Double getRateByMessageIdAndUserId(Long messageId, Long userId);
}
