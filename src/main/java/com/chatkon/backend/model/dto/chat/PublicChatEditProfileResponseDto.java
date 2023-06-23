package com.chatkon.backend.model.dto.chat;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class PublicChatEditProfileResponseDto extends ActionDto {
    ChatDto chat;
}
