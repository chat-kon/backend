package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageReplayResponseDto extends ActionDto {
    MessageDto messageDto;
    MessageDto messageRefDto;
}
