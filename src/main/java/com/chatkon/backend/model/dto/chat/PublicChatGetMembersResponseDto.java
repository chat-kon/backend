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
public class PublicChatGetMembersResponseDto extends ActionDto {
    Set<UserDto> users;
    Set<MemberDto> members;
    Set<AdminDto> admins;
}
