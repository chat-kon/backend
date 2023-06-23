package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.PublicChatJoinWithLinkRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatJoinWithLinkResponseDto;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.repository.PublicChatRepository;
import com.chatkon.backend.service.chat.PublicChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicChatJoinWithLinkHandler implements ActionHandler<PublicChatJoinWithLinkRequestDto> {
    private final PublicChatService publicChatService;
    private final PublicChatRepository publicChatRepository;

    @Override
    public ActionType type() {
        return ActionType.JOIN_CHAT_WITH_LINK;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatJoinWithLinkRequestDto dto) {

        PublicChat publicChat = publicChatRepository.findPublicChatByLink(dto.getLink());
        PublicChat chat = publicChatService.joinPublicChat(publicChat.getId(), userId);

        PublicChatJoinWithLinkResponseDto responseDto = PublicChatJoinWithLinkResponseDto.builder()
                .chatId(chat.getId())
                .userId(userId)
                .build();

        Action action = Action.builder()
                .type(ActionType.JOIN_CHAT_WITH_LINK)
                .dto(responseDto).build();

        Set<Long> receivers = publicChatService.getChatMembers(chat.getId()).stream()
                .map(User::getId).collect(Collectors.toSet());

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
                .build();
    }

}
