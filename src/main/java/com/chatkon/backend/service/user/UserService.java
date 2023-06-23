package com.chatkon.backend.service.user;

import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.user.User;

import java.util.Set;

public interface UserService {
    User findUser(Long userId);

    User saveUser(User user);

    void deleteUser(Long userId);

    Set<Chat> getUserChats(Long userId);

    User findUser(String username, String password);

    User findUser(String username);

    User editProfile(User user, Long userId);
}
