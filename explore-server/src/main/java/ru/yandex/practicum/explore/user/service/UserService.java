package ru.yandex.practicum.explore.user.service;

import ru.yandex.practicum.explore.user.model.User;
import ru.yandex.practicum.explore.user.dto.UserIncomeDto;
import ru.yandex.practicum.explore.user.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    UserDto addUser(UserIncomeDto body);

    void deleteUserById(Long userId);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}
