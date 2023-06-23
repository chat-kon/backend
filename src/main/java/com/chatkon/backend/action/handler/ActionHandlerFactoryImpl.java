package com.chatkon.backend.action.handler;

import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.exception.NotImplementedException;
import com.chatkon.backend.model.dto.ActionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ActionHandlerFactoryImpl implements ActionHandlerFactory {
    private final Set<ActionHandler<? extends ActionDto>> handlers;

    @Override
    @SuppressWarnings("unchecked")
    public ActionHandler<ActionDto> getHandler(ActionType actionType) {
        return handlers.stream()
                .filter(handler -> handler.type().equals(actionType))
                .findFirst()
                .map(handler -> (ActionHandler<ActionDto>) handler)
                .orElseThrow(NotImplementedException::new);
    }
}
