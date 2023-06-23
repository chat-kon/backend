package com.chatkon.backend.action.handler;

import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.model.dto.ActionDto;

public interface ActionHandlerFactory {

    ActionHandler<ActionDto> getHandler(ActionType actionType);

}
