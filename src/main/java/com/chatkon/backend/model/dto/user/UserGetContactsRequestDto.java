package com.chatkon.backend.model.dto.user;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGetContactsRequestDto extends ActionDto {
}
