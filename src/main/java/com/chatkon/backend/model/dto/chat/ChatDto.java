package com.chatkon.backend.model.dto.chat;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.entity.chat.ChatType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatDto extends ActionDto {
    private Long id;
    private Long ownerId;
    private String title;
    private ChatType type;
    private String avatar;
    private Long user1Id;
    private Long user2Id;
    private Long createdAt;
}