package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.PublicChatDeleteMemberRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatDeleteMemberResponseDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.service.user.UserService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicChatDeleteMemberHandler implements ActionHandler<PublicChatDeleteMemberRequestDto> {
    private final PublicChatService publicChatService;
    private final UserService userService;

    @Override
    public ActionType type() {
        return ActionType.DELETE_MEMBER;
    }

    @Override
    public ActionResult handle(Long deleterId, PublicChatDeleteMemberRequestDto dto) {

        Set<Long> receivers = publicChatService.getChatMembers(dto.getChatId()).stream()
                .map(User::getId).collect(Collectors.toSet());

        PublicChat chat = publicChatService.deleteMember(dto.getChatId(), deleterId, dto.getUserId());

        User user = userService.findUser(dto.getUserId());
        User deleter = userService.findUser(deleterId);

        PublicChatDeleteMemberResponseDto responseDto = PublicChatDeleteMemberResponseDto.builder()
                .chatId(chat.getId())
                .user(Mapper.map(user, UserDto.class))
                .deleter(Mapper.map(deleter, UserDto.class))
                .build();

        Action action = Action.builder()
                .type(ActionType.DELETE_MEMBER)
                .dto(responseDto).build();

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
                .build();
    }
}
