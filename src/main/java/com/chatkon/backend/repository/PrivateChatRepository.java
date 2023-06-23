package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.chat.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PrivateChatRepository extends JpaRepository<PrivateChat, Long> {

    Optional<PrivateChat> findPrivateChatByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    Set<PrivateChat> findPrivateChatsByUser1Id(Long userId);

    Set<PrivateChat> findPrivateChatsByUser2Id(Long userId);
}