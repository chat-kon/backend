package com.chatkon.backend.model.dto.chat;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.user.UserDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicChatDeleteMemberResponseDto extends ActionDto {
    Long chatId;
    UserDto user;
    UserDto deleter;
}