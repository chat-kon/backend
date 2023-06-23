package com.chatkon.backend.action.handler.user;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.user.UserSignUpRequestDto;
import com.chatkon.backend.model.dto.user.UserSignUpResponseDto;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.model.entity.user.UserSession;
import com.chatkon.backend.service.user.SessionService;
import com.chatkon.backend.service.user.UserService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSignUpHandler implements ActionHandler<UserSignUpRequestDto> {
    private final UserService userService;
    private final SessionService sessionService;

    @Override
    public ActionType type() {
        return ActionType.SIGN_UP;
    }

    @Override
    public ActionResult handle(Long userId, UserSignUpRequestDto dto) {

        User mappedUser = Mapper.map(dto, User.class);
        User newUser = userService.saveUser(mappedUser);

        UserSession session = sessionService.createUserSession(newUser);

        UserSignUpResponseDto responseDto = UserSignUpResponseDto.builder()
                .userId(newUser.getId())
                .build();

        Action action = Action.builder()
                .type(type())
                .token(session.getId())
                .dto(responseDto).build();

        Set<Long> receivers = Set.of(newUser.getId());

        return ActionResult.builder()
                .action(action)
                .receivers(receivers).build();
    }
}
