package com.chatkon.backend.action.handler.message;


import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.exception.NotImplementedException;
import com.chatkon.backend.model.dto.message.MessageAddCommentRequestDto;
import com.chatkon.backend.model.dto.message.MessageAddCommentResponseDto;
import com.chatkon.backend.model.dto.message.MessageForwardRequestDto;
import com.chatkon.backend.model.dto.message.MessageReplayResponseDto;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.message.TextMessage;
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
public class MessageAddCommentHandler implements ActionHandler<MessageAddCommentRequestDto> {
    private final MessageService messageService;
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.ADD_COMMENT_MESSAGE;
    }

    @Override
    public ActionResult handle(Long userId, MessageAddCommentRequestDto dto) {
        // TODO implement it

        Message message = messageService.findMessage(dto.getMessageRefId());
        Set<Long> receivers = getReceivers(message);

        // call service

        MessageAddCommentResponseDto responseDto = MessageAddCommentResponseDto.builder()
                .build();

        Action action = Action.builder()
                .dto(responseDto)
                .type(ActionType.ADD_COMMENT_MESSAGE)
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
