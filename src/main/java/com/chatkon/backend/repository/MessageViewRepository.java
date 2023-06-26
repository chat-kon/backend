package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.message.MessageRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageViewRepository extends JpaRepository<MessageRate, Long> {
    @Modifying
    @Query("UPDATE MessageView mv SET mv.view = mv.view + 1 WHERE mv.user.id = :userId AND mv.message.id = :messageId")
    void incrementViewCount(Long userId, Long messageId);

    @Query("SELECT SUM(mv.view) FROM MessageView mv WHERE mv.message.id = :messageId")
    Optional<Long> getViewCountByMessageId(Long messageId);
}
