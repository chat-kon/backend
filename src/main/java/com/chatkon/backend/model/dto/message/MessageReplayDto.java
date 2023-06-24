package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.user.UserForwardDto;
import com.chatkon.backend.model.entity.message.MessageType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageReplayDto extends ActionDto {
    Long id;
    Long senderId;
    String text;
    String name;
    String caption;
    MessageType type;
}
