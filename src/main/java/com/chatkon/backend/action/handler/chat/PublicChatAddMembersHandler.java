package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.chat.PublicChatAddMembersRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatAddMembersResponseDto;
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
public class PublicChatAddMembersHandler implements ActionHandler<PublicChatAddMembersRequestDto> {
    private final PublicChatService publicChatService;
    private final UserService userService;

    @Override
    public ActionType type() {
        return ActionType.ADD_NEW_MEMBERS;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatAddMembersRequestDto dto) {

        Set<User> addedUsers = publicChatService.addMembers(dto.getChatId(), userId, dto.getUserIds());

        Set<UserDto> users = addedUsers.stream()
                .map(user -> Mapper.map(user, UserDto.class)).collect(Collectors.toSet());

        PublicChat chat = publicChatService.findPublicChat(dto.getChatId());

        PublicChatAddMembersResponseDto responseDto = PublicChatAddMembersResponseDto.builder()
                .chat(Mapper.map(chat, ChatDto.class))
                .users(users)
                .adder(Mapper.map(userService.findUser(userId), UserDto.class))
                .build();

        Action action = Action.builder()
                .type(ActionType.ADD_NEW_MEMBERS)
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
