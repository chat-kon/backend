package com.chatkon.backend.model.dto.user;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class UserEditProfileRequestDto extends ActionDto {
    UserDto userDto;
    boolean accessAddPublicChat;
    boolean visibleAvatar;
}