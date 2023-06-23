package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.AdminDto;
import com.chatkon.backend.model.dto.chat.MemberDto;
import com.chatkon.backend.model.dto.chat.PublicChatGetMembersRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatGetMembersResponseDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicChatGetMembersHandler implements ActionHandler<PublicChatGetMembersRequestDto> {
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.GET_PUBLIC_CHAT_MEMBERS;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatGetMembersRequestDto dto) {

        Set<UserDto> users = publicChatService.getChatMembers(dto.getChatId()).stream()
                .map(user -> Mapper.map(user, UserDto.class)).collect(Collectors.toSet());

        Set<MemberDto> members = users.stream()
                .map(user -> MemberDto.builder()
                        .chatId(dto.getChatId())
                        .userId(user.getId())
                        .build())
                .collect(Collectors.toSet());

        Set<AdminDto> admins = publicChatService.getChatAdmins(dto.getChatId()).stream()
                .map(user -> AdminDto.builder()
                        .chatId(dto.getChatId())
                        .userId(user.getId())
                        .build())
                .collect(Collectors.toSet());

        PublicChatGetMembersResponseDto responseDto = PublicChatGetMembersResponseDto.builder()
                .users(users)
                .members(members)
                .admins(admins)
                .build();

        Action action = Action.builder()
                .type(ActionType.GET_PUBLIC_CHAT_MEMBERS)
                .dto(responseDto)
                .build();

        return ActionResult.builder()
                .action(action)
                .receivers(Set.of(userId)).build();
    }
}
