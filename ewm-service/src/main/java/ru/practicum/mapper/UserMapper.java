package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User saveUser) {
        return UserDto.builder()
                .id(saveUser.getId())
                .name(saveUser.getName())
                .email(saveUser.getEmail())
                .build();
    }

    public User toUser(NewUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }
}
