package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.chat.PublicChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicChatRepository extends JpaRepository<PublicChat, Long> {
    PublicChat findPublicChatByLink(String link);
}
