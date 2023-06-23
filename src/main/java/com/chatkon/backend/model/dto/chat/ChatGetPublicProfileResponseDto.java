package com.chatkon.backend.model.dto.chat;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.user.UserDto;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatGetPublicProfileResponseDto extends ActionDto {
    Set<UserDto> members;
    Set<Long> adminIds;
    Long OwnerId;
}