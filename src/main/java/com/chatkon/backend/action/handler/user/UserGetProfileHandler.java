package com.chatkon.backend.action.handler.user;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.dto.user.UserViewProfileRequestDto;
import com.chatkon.backend.model.dto.user.UserViewProfileResponseDto;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.user.UserService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserGetProfileHandler implements ActionHandler<UserViewProfileRequestDto> {
    private final UserService userService;

    @Override
    public ActionType type() {
        return ActionType.VIEW_USER_PROFILE;
    }

    @Override
    public ActionResult handle(Long userId, UserViewProfileRequestDto dto) {

        User user = userService.findUser(dto.getUsername());

        UserDto userDto = Mapper.map(user, UserDto.class);

        // todo check this in service
        if (!user.getVisibleAvatar()) {
            userDto.setAvatar(null);
        }

        UserViewProfileResponseDto responseDto = UserViewProfileResponseDto.builder()
                .user(userDto)
                .build();

        Action action = Action.builder()
                .type(type())
                .dto(responseDto)
                .build();

        Set<Long> receivers = Set.of(userId);

        return ActionResult.builder()
                .action(action)
                .receivers(receivers).build();
    }
}
