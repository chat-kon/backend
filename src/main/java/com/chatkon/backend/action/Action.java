package com.chatkon.backend.action;

import com.chatkon.backend.model.dto.ActionDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Action {
    private Long id;
    private ActionType type;
    private ActionDto dto;
    private String token;
    private String error;
}

