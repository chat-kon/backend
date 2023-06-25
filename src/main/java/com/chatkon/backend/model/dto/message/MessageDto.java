package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.dto.user.UserForwardDto;
import com.chatkon.backend.model.entity.message.MessageType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageDto extends ActionDto {
    Long id;
    Long senderId;
    Long chatId;
    UserForwardDto userForwardDto;
    MessageReplayDto messageReplayDto;
    String text;
    String name;
    Byte[] data;
    String caption;
    Long date;
    MessageType type;
    Double averageRate;
    Double userRate;
}
