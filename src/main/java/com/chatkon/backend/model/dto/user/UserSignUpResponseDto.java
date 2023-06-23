package com.chatkon.backend.model.dto.user;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSignUpResponseDto extends ActionDto {
    Long userId;
}
