package com.chatkon.backend.service.message;


import com.chatkon.backend.exception.BadRequestException;
import com.chatkon.backend.exception.ChatNotFoundException;
import com.chatkon.backend.exception.MessageNotFoundException;
import com.chatkon.backend.exception.UserNotFoundException;
import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.chat.ChatType;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.message.*;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final MessageRateRepository messageRateRepository;

    @Override
    public TextMessage createText(Long chatId, Long senderId, TextMessage textMessage) {
        User sender = findUser(senderId);
        Chat chat = findChat(chatId);

        if (chat instanceof PublicChat) {
            checkPublicChatAccess(sender, (PublicChat) chat);
        } else if (chat instanceof PrivateChat) {
            checkPrivateChatAccess(sender, (PrivateChat) chat);
        }

        textMessage.setSender(sender);
        textMessage.setChat(chat);

        textMessage.setDate(System.currentTimeMillis());
        textMessage.setId(null);
        textMessage.setMessageType(MessageType.TEXT);

        return messageRepository.save(textMessage);
    }

    @Override
    public BinaryMessage saveFile(Long chatId, Long senderId, BinaryMessage binaryMessage) {
        if (binaryMessage.getMessageType() == null || binaryMessage.getMessageType().equals(MessageType.TEXT)) {
            throw new BadRequestException("MessageType " + binaryMessage.getMessageType() + " is either null or not binary.");
        }

        User sender = findUser(senderId);
        Chat chat = findChat(chatId);

        if (chat instanceof PublicChat) {
            checkPublicChatAccess(sender, (PublicChat) chat);
        } else if (chat instanceof PrivateChat) {
            checkPrivateChatAccess(sender, (PrivateChat) chat);
        }

        binaryMessage.setSender(sender);
        binaryMessage.setChat(chat);

        binaryMessage.setDate(System.currentTimeMillis());
        binaryMessage.setId(null);

        return messageRepository.save(binaryMessage);
    }

    @Override
    public Message getLastMessage(Long chatId) {
        return messageRepository.getLastMessageByChatId(chatId);
    }

    private Chat findChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(ChatNotFoundException::new);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkPublicChatAccess(User sender, PublicChat chat) {

        boolean isMember = memberRepository.findByChatIdAndUserId(chat.getId(), sender.getId()).isPresent();

        if (!isMember)
            throw new BadRequestException();

        boolean isChannel = chat.getType() == ChatType.CHANNEL;

        if (isChannel) {

            boolean isOwner = chat.getOwner().equals(sender);
            boolean isAdmin = adminRepository.findByChatIdAndUserId(chat.getId(), sender.getId()).isPresent();

            if (!isOwner && !isAdmin)
                throw new BadRequestException();
        }

        //todo check sending message is allowed in chat
    }

    private void checkPrivateChatAccess(User sender, PrivateChat chat) {

        // todo check sender is not blocked by receiver user
    }

    @Transactional
    @Override
    public Long deleteMessage(Long deleterId, Long messageId) {
        Message message = findMessage(messageId);
        User sender = message.getSender();
        Chat chat = message.getChat();
        User deleter = findUser(deleterId);

        if (chat instanceof PrivateChat) {
            if (!deleter.equals(sender)) {
                throw new BadRequestException();
            }
        } else if (chat instanceof PublicChat publicChat) {
            User owner = publicChat.getOwner();
            if (!deleter.equals(owner) && !deleter.equals(sender)) {
                throw new BadRequestException();
            }
        }
        messageRepository.deleteMessageById(messageId);
        return chat.getId();
    }

    @Override
    public Message findMessage(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public MessageRate rateMessage(Long userId, Long messageId, Double rate) {
        var user = findUser(userId);
        Message message = findRefMessageOrThis(messageId);

        MessageRate messageRate = MessageRate
                .builder()
                .user(user)
                .message(message)
                .rate(rate)
                .build();

        return messageRateRepository.save(messageRate);
    }

    private Message findRefMessageOrThis(Long messageId) {
        var message = findMessage(messageId);
        var forwardMessage = message.getForwardMessageRef();
        var replayMessage = message.getReplyMessageRef();

        if (forwardMessage != null)
            message = forwardMessage;
        else if (replayMessage != null)
            message = replayMessage;

        return message;
    }

    @Override
    public Double getAverageRate(Long messageId) {
        Message message = findRefMessageOrThis(messageId);
        return messageRateRepository.getAverageRateByMessageId(message.getId()).orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public Double getUserRateOnMessage(Long userId, Long messageId) {
        Message message = findRefMessageOrThis(messageId);
        return messageRateRepository.getRateByMessageIdAndUserId(message.getId(), userId);
    }

    @Override
    public TextMessage forwardMessage(Long userId, Long messageId, Long chatId) {
        var chat = findChat(chatId);
        var user = findUser(userId);
        TextMessage refMessage = (TextMessage) findMessage(messageId);

        var newMessage = TextMessage.builder()
                .text(refMessage.getText())
                .messageType(refMessage.getMessageType())
                .forwardMessageRef(refMessage)
                .sender(user)
                .chat(chat)
                .date(System.currentTimeMillis())
                .build();

        return messageRepository.save(newMessage);
    }
}
