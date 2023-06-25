package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.entity.message.MessageType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageCommentDto extends ActionDto {
    Long id;
    Long senderId;
    Long messageRefId;
    String text;
    String name;
    String caption;
    Long date;
    MessageType type;
}
