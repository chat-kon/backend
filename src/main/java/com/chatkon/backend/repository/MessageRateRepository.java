package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.message.MessageRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRateRepository extends JpaRepository<MessageRate, Long> {
    Double getAverageRateByMessageId(Long messageId);
}
