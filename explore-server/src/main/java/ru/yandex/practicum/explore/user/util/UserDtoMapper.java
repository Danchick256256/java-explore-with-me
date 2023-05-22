package ru.yandex.practicum.explore.user.util;

import ru.yandex.practicum.explore.user.dto.UserShortDto;
import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.user.dto.UserDto;
import ru.yandex.practicum.explore.user.dto.UserIncomeDto;

public class UserDtoMapper {
    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto userToShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User newDtoToUser(UserIncomeDto body) {
        return User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .build();
    }
}
