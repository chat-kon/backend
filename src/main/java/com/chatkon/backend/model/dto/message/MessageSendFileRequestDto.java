package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.entity.message.MessageType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageSendFileRequestDto extends ActionDto {
    MessageType messageType;
    Byte[] data;
    String name;
    String caption;
    Long toId;
    boolean isPrivate;
}
