package ru.yandex.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore.user.dto.UserDto;
import ru.yandex.practicum.explore.user.dto.UserIncomeDto;
import ru.yandex.practicum.explore.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAdmins(@RequestParam(name = "ids", defaultValue = "") List<Long> ids,
                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size,
                                   HttpServletRequest request) {
        log.info("{className: {}, method: {GET: {}}, data: {ids: [{}], from: {}, size: {}}}",
                getClass().getName(), request.getRequestURI(), ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto addAdmin(@RequestBody @Valid UserIncomeDto body,
                            HttpServletRequest request) {
        log.info("{className: {}, method: {POST: {}}, data: {{}}}",
                getClass().getName(), request.getRequestURI(), body);
        return userService.addUser(body);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long userId,
                                            HttpServletRequest request) {
        log.info("{className: {}, method: {DELETE: {}}, data: {userId: {}}",
                getClass().getName(), request.getRequestURI(), userId);
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
