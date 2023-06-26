package com.chatkon.backend.action.handler.message;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.exception.NotImplementedException;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.message.MessageDto;
import com.chatkon.backend.model.dto.message.MessageSendTextRequestDto;
import com.chatkon.backend.model.dto.message.MessageSendTextResponseDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.message.TextMessage;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.PrivateChatService;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.service.message.MessageService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageSendTextHandler implements ActionHandler<MessageSendTextRequestDto> {
    private final MessageService messageService;
    private final PrivateChatService privateChatService;
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.SEND_TEXT;
    }

    @Override
    public ActionResult handle(Long userId, MessageSendTextRequestDto dto) {
        dto.setPrivate(true);

        Long chatId;
        if (dto.isPrivate()) {

            long receiverId = dto.getToId();
            Optional<PrivateChat> chat = privateChatService.findPrivateChat(userId, receiverId);

            if (chat.isEmpty()) {
                chat = Optional.of(privateChatService.createPrivateChat(userId, receiverId));
            }

            chatId = chat.get().getId();

        } else {
            chatId = dto.getToId();
        }

        TextMessage newMessage = messageService.createText(
                chatId,
                userId,
                Mapper.map(dto, TextMessage.class)
        );

        Action action = getAction(newMessage);
        Set<Long> receivers = getReceivers(newMessage);

        return ActionResult.builder()
                .receivers(receivers)
                .action(action)
                .build();
    }

    private Action getAction(Message message) {

        User sender = message.getSender();
        Chat chat = message.getChat();

        ChatDto chatDto = Mapper.map(chat, ChatDto.class);
        UserDto userDto = Mapper.map(sender, UserDto.class);
        MessageDto messageDto = Mapper.map(message, MessageDto.class);

        messageDto.setSenderId(sender.getId());
        messageDto.setChatId(chat.getId());

        if (chat instanceof PrivateChat privateChat) {
            User receiver = privateChat.getParticipant(sender.getId());
            chatDto.setUser1Id(sender.getId());
            chatDto.setUser2Id(receiver.getId());
        }

        MessageSendTextResponseDto newMessageDto = MessageSendTextResponseDto.builder()
                .chat(chatDto)
                .user(userDto)
                .message(messageDto)
                .build();

        return Action.builder()
                .type(ActionType.SEND_TEXT)
                .dto(newMessageDto)
                .build();
    }

    private Set<Long> getReceivers(Message message) {
        Set<Long> receivers;

        if (message.getChat() instanceof PrivateChat chat) {
            receivers = new HashSet<>(List.of(chat.getUser1().getId(), chat.getUser2().getId()));

        } else if (message.getChat() instanceof PublicChat chat) {
            receivers = publicChatService.getChatMembers(chat.getId()).stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
        } else {
            throw new NotImplementedException();
        }

        return receivers;
    }
}
