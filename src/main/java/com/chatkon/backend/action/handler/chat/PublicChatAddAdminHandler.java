package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.PublicChatSelectNewAdminRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatSelectNewAdminResponseDto;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.PublicChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicChatAddAdminHandler implements ActionHandler<PublicChatSelectNewAdminRequestDto> {
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.SELECT_NEW_ADMIN_PUBLIC_CHAT;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatSelectNewAdminRequestDto dto) {

        publicChatService.addAdmin(dto.getChatId(), userId, dto.getUserId());

        PublicChatSelectNewAdminResponseDto responseDto = PublicChatSelectNewAdminResponseDto.builder()
                .chatId(dto.getChatId())
                .userId(dto.getUserId())
                .build();

        Action action = Action.builder()
                .type(ActionType.SELECT_NEW_ADMIN_PUBLIC_CHAT)
                .dto(responseDto)
                .build();

        Set<Long> receivers = publicChatService.getChatMembers(dto.getChatId()).stream()
                .map(User::getId).collect(Collectors.toSet());

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
                .build();
    }
}
