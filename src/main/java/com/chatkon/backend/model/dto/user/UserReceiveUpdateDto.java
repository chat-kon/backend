package com.chatkon.backend.model.dto.user;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.chat.ChatReceiveProfileUpdateDto;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserReceiveUpdateDto extends ActionDto {

    Set<ChatReceiveProfileUpdateDto> chat;
}
