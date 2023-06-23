package com.chatkon.backend.service.chat;

import com.chatkon.backend.exception.ChatNotFoundException;
import com.chatkon.backend.exception.UserNotFoundException;
import com.chatkon.backend.model.entity.chat.ChatType;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.repository.PrivateChatRepository;
import com.chatkon.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateChatServiceImpl implements PrivateChatService {
    private final PrivateChatRepository privateChatRepository;
    private final UserRepository userRepository;

    @Override
    public PrivateChat findPrivateChat(Long chatId) {
        return privateChatRepository.findById(chatId)
                .orElseThrow(ChatNotFoundException::new);
    }

    @Override
    public PrivateChat createPrivateChat(Long user1Id, Long user2Id) {
        User user1 = findUser(user1Id);
        User user2 = findUser(user2Id);

        PrivateChat chat = PrivateChat.builder()
                .user1(user1)
                .user2(user2)
                .type(ChatType.PRIVATE)
                .createdAt(System.currentTimeMillis())
                .build();

        return privateChatRepository.save(chat);
    }

    @Override
    public Optional<PrivateChat> findPrivateChat(Long user1Id, Long user2Id) {
        // todo check if needed to change order of ids
        return privateChatRepository.findPrivateChatByUser1IdAndUser2Id(user1Id, user2Id);
//                .orElseThrow(ChatNotFoundException::new);
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
