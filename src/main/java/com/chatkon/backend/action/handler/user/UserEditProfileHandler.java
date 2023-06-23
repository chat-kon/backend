package com.chatkon.backend.action.handler.user;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.dto.user.UserEditProfileRequestDto;
import com.chatkon.backend.model.dto.user.UserEditProfileResponseDto;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.user.UserService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserEditProfileHandler implements ActionHandler<UserEditProfileRequestDto> {
    private final UserService userService;

    @Override
    public ActionType type() {
        return ActionType.EDIT_PROFILE;
    }

    @Override
    public ActionResult handle(Long userId, UserEditProfileRequestDto dto) {

        User user = Mapper.map(dto, User.class);

        user.setAllowedInvite(dto.isAccessAddPublicChat());

        user.setVisibleAvatar(dto.isVisibleAvatar());

        User editedUser = userService.editProfile(user, userId);

        UserEditProfileResponseDto responseDto = UserEditProfileResponseDto.builder()
                .userDto(Mapper.map(editedUser, UserDto.class))
                .accessAddPublicChat(editedUser.getAllowedInvite())
                .visibleAvatar(editedUser.getVisibleAvatar())
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
