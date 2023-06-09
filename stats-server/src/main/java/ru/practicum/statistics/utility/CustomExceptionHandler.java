package ru.practicum.statistics.utility;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor
public class CustomExceptionHandler {
    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(Exception exception) {
        String reason = "For the requested operation the conditions are not met.";
        log.warn("{STATUS: {}, reason: {}, message: {}, time: {}}", HttpStatus.BAD_REQUEST.name(), reason, exception.getMessage(), LocalDateTime.now());
        return new ErrorResponse(exception.getStackTrace(), exception.getMessage(), reason,
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }
}
