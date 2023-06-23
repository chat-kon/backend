package com.chatkon.backend.model.dto.chat;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.message.MessageDto;
import com.chatkon.backend.model.dto.user.UserDto;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatGetMessagesResponseDto extends ActionDto {
    Set<UserDto> users;
    Set<MessageDto> messages;
}
