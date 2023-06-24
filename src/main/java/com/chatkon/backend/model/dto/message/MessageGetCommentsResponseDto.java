package com.chatkon.backend.model.dto.message;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageGetCommentsResponseDto extends ActionDto {
    Set<MessageCommentDto> messageCommentsDto;
}