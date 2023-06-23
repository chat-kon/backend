package com.chatkon.backend.action.handler.user;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.user.UserSignInRequestDto;
import com.chatkon.backend.model.dto.user.UserSignInResponseDto;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.model.entity.user.UserSession;
import com.chatkon.backend.service.user.SessionService;
import com.chatkon.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSignInHandler implements ActionHandler<UserSignInRequestDto> {
    private final UserService userService;
    private final SessionService sessionService;

    @Override
    public ActionType type() {
        return ActionType.SIGN_IN;
    }

    @Override
    public ActionResult handle(Long userId, UserSignInRequestDto dto) {

        User user = userService.findUser(dto.getUsername(), dto.getPassword());

        UserSession session = sessionService.createUserSession(user);

        UserSignInResponseDto responseDto = UserSignInResponseDto.builder()
                .userId(user.getId())
                .build();

        Action action = Action.builder()
                .type(type())
                .token(session.getId())
                .dto(responseDto).build();

        Set<Long> receivers = Set.of(user.getId());

        return ActionResult.builder()
                .action(action)
                .receivers(receivers).build();
    }
}
