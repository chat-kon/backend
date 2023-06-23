package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.PrivateChatDeleteRequestDto;
import com.chatkon.backend.model.dto.chat.PrivateChatDeleteResponseDto;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.service.chat.ChatService;
import com.chatkon.backend.service.chat.PrivateChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PrivateChatDeleteHandler implements ActionHandler<PrivateChatDeleteRequestDto> {
    private final PrivateChatService privateChatService;
    private final ChatService chatService;

    @Override
    public ActionType type() {
        return ActionType.DELETE_PRIVATE_CHAT;
    }

    @Override
    public ActionResult handle(Long userId, PrivateChatDeleteRequestDto dto) {

        PrivateChat chat = privateChatService.findPrivateChat(dto.getChatId());

        chatService.deleteChat(userId, dto.getChatId());

        Set<Long> receivers = new HashSet<>(List.of(chat.getUser1().getId(), chat.getUser2().getId()));

        PrivateChatDeleteResponseDto responseDto = PrivateChatDeleteResponseDto.builder()
                .chatId(dto.getChatId())
                .build();

        Action action = Action.builder()
                .type(ActionType.DELETE_PRIVATE_CHAT)
                .dto(responseDto)
                .build();

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
                .build();
    }
}
