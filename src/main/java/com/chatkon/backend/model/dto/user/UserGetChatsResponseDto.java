package com.chatkon.backend.model.dto.user;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.message.MessageDto;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGetChatsResponseDto extends ActionDto {
    Set<UserDto> users;
    Set<ChatDto> chats;
    Set<MessageDto> messages;
}
