package com.chatkon.backend.service.chat;

import com.chatkon.backend.exception.BadRequestException;
import com.chatkon.backend.exception.ChatNotFoundException;
import com.chatkon.backend.exception.NotImplementedException;
import com.chatkon.backend.exception.UserNotFoundException;
import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;
    private final PrivateChatRepository privateChatRepository;
    private final MessageViewRepository messageViewRepository;

    @Override
    public Chat findChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
    }

    @Override
    public Set<Message> getChatMessages(Long userId, Long chatId) {
        Chat chat = findChat(chatId);

        boolean isMember;

        if(chat instanceof PrivateChat privateChat) {
            isMember = privateChat.getUser1().getId().equals(userId) || privateChat.getUser2().getId().equals(userId);
        } else if (chat instanceof PublicChat) {
            isMember = memberRepository.findByChatIdAndUserId(chatId, userId).isPresent();
        } else {
            throw new NotImplementedException();
        }

        if (!isMember)
            throw new BadRequestException();

        var messages = messageRepository.findMessagesByChatId(chatId);
        messages.forEach(message -> messageViewRepository.incrementViewCount(userId, message.getId()));
        return messages;
    }

    @Transactional
    @Override
    public void deleteChat(Long userId, Long chatId) {
        Chat chat = findChat(chatId);

        if (chat instanceof PrivateChat privateChat) {
            boolean isUserInChat =
                    List.of(privateChat.getUser1().getId(), privateChat.getUser2().getId()).contains(userId);

            if (!isUserInChat) {
                throw new BadRequestException();
            }

        } else if (chat instanceof PublicChat publicChat) {

            if (!publicChat.getOwner().getId().equals(userId)) {
                throw new BadRequestException();
            }

            adminRepository.deleteByIdChatId(chatId);
            memberRepository.deleteByChatId(chatId);
        }

        messageRepository.deleteByChatId(chatId);
        chatRepository.deleteById(chatId);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}

