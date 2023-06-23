package com.chatkon.backend.action.handler;

import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.model.dto.ActionDto;

public interface ActionHandler<T extends ActionDto> {

    ActionType type();

    ActionResult handle(Long userId, T dto);

}
