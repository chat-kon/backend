package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.user.UserDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageSendTextResponseDto extends ActionDto {
    ChatDto chat;
    UserDto user;
    MessageDto message;
}
