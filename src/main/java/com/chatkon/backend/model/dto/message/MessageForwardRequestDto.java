package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageForwardRequestDto extends ActionDto {
    // TODO we should copy from this message, and change: forwardedFrom = sender | sender = this user id | update date
    Long messageId;
    Long receiverId;
}
