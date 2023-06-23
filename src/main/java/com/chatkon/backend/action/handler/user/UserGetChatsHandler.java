package com.chatkon.backend.action.handler.user;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.message.MessageDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.dto.user.UserGetChatsRequestDto;
import com.chatkon.backend.model.dto.user.UserGetChatsResponseDto;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.message.MessageService;
import com.chatkon.backend.service.user.UserService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserGetChatsHandler implements ActionHandler<UserGetChatsRequestDto> {
    private final UserService userService;
    private final MessageService messageService;

    @Override
    public ActionType type() {
        return ActionType.GET_USER_CHATS;
    }

    @Override
    public ActionResult handle(Long userId, UserGetChatsRequestDto dto) {

        UserGetChatsResponseDto responseDto = generateResponse(userId);

        Action action = Action.builder()
                .type(type())
                .dto(responseDto).build();

        Set<Long> receivers = Set.of(userId);

        return ActionResult.builder()
                .action(action)
                .receivers(receivers).build();
    }

    private UserGetChatsResponseDto generateResponse(Long userId) {

        Set<ChatDto> chats = new HashSet<>();
        Set<UserDto> users = new HashSet<>();
        Set<MessageDto> messages = new HashSet<>();

        User self = userService.findUser(userId);
        users.add(Mapper.map(self, UserDto.class));

        userService.getUserChats(userId).forEach(chat -> {

            ChatDto chatDto = Mapper.map(chat, ChatDto.class);
            chats.add(chatDto);

            if (chat instanceof PrivateChat privateChat) {
                User user = privateChat.getParticipant(userId);
                chatDto.setUser1Id(userId);
                chatDto.setUser2Id(user.getId());

                UserDto userDto = Mapper.map(user, UserDto.class);
                users.add(userDto);
            }

            var lastMessage = messageService.getLastMessage(chat.getId());

            if (lastMessage != null) {
                if (chat instanceof PublicChat) {
                    UserDto userDto = Mapper.map(lastMessage.getSender(), UserDto.class);
                    users.add(userDto);
                }

                MessageDto messageDto = Mapper.map(lastMessage, MessageDto.class);
                messageDto.setSenderId(lastMessage.getSender().getId());
                messageDto.setChatId(lastMessage.getChat().getId());
                messages.add(messageDto);
            }
        });

        return UserGetChatsResponseDto.builder()
                .users(users)
                .chats(chats)
                .messages(messages)
                .build();
    }
}