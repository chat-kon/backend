package com.chatkon.backend.model.dto.user;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserChatDto extends ActionDto {
    Long chatId;
    Long userId;
    String title;
    String username;
    String lastMessage;
    Long date;
}
