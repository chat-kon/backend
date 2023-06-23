package com.chatkon.backend.repository;

import com.chatkon.backend.model.entity.user.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SessionRepository extends JpaRepository<UserSession, String> {
    Set<UserSession> findUserSessionsByUserId(Long userId);
}
