package ru.yandex.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.explore.exception.NotFoundException;
import ru.yandex.practicum.explore.user.repository.UserRepository;
import ru.yandex.practicum.explore.user.dto.UserIncomeDto;
import ru.yandex.practicum.explore.user.dto.UserDto;
import ru.yandex.practicum.explore.user.util.UserDtoMapper;
import ru.yandex.practicum.explore.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.explore.user.util.UserDtoMapper.newDtoToUser;
import static ru.yandex.practicum.explore.user.util.UserDtoMapper.userToDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User with id=%s was not found.", userId)));
    }

    @Override
    @Transactional
    public UserDto addUser(UserIncomeDto body) {
        return userToDto(userRepository.save(newDtoToUser(body)));
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null && ids.size() > 0) {
            return userRepository.findAllByIds(ids, PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(UserDtoMapper::userToDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(UserDtoMapper::userToDto)
                    .collect(Collectors.toList());
        }
    }
}
