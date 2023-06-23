package com.chatkon.backend.action.handler.message;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.exception.NotImplementedException;
import com.chatkon.backend.model.dto.message.MessageDeleteRequestDto;
import com.chatkon.backend.model.dto.message.MessageDeleteResponseDto;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageDeleteHandler implements ActionHandler<MessageDeleteRequestDto> {
    private final MessageService messageService;
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.DELETE_MESSAGE;
    }

    @Override
    public ActionResult handle(Long userId, MessageDeleteRequestDto dto) {

        Message message = messageService.findMessage(dto.getMessageId());
        Set<Long> receivers = getReceivers(message);

        Long chatId = messageService.deleteMessage(userId, dto.getMessageId());

        MessageDeleteResponseDto responseDto = MessageDeleteResponseDto.builder()
                .messageId(dto.getMessageId())
                .chatId(chatId).build();

        Action action = Action.builder()
                .dto(responseDto)
                .type(ActionType.DELETE_MESSAGE)
                .build();

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
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
